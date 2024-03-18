package io.github.x45iq.jtube;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ContainersApiTools {
    static final String API_TEMPLATE = "https://www.youtube.com%s?key=%s&prettyPrint=false";
    static final Pattern API_KEY_REGEX = Pattern.compile("\"INNERTUBE_API_KEY\":\\s*\"(.+?)\",");
    static final Pattern INITIAL_DATA_REGEX = Pattern.compile("var\\s*ytInitialData\\s*=\\s*(\\{.*\\});");
    static final Pattern CONTEXT_REGEX = Pattern.compile("ytcfg.set\\((\\{.*\\})\\);");

    private ContainersApiTools() {

    }

    static JsonObject parseInitialData(Document document) throws ResponseParsingException {
        assert document != null;
        Matcher mat;
        for (Element el : document.select("script")) {
            if ((mat = INITIAL_DATA_REGEX.matcher(el.data())).find()) {
                return new Gson().fromJson(mat.group(1), JsonObject.class);
            }
        }
        throw new ResponseParsingException("Initial data not found");
    }

    static List<ContainerVideo> parseVideos(Document document, JsonArray items, Function<JsonObject, ContainerVideo> parseFunc) throws ResponseParsingException, IOException {
        assert document != null;
        assert items != null;
        assert parseFunc != null;
        List<ContainerVideo> videos = new ArrayList<>();
        Matcher mat;
        if (!(mat = API_KEY_REGEX.matcher(document.html())).find()) {
            throw new ResponseParsingException("API key not found");
        }
        String apiKey = mat.group(1);
        JsonObject context = parseContext(document);

        while (true) {
            for (int i = 0; i < items.size() - 1; i++) {
                videos.add(parseFunc.apply(items.get(i).getAsJsonObject()));
            }
            if (items.get(items.size() - 1).getAsJsonObject().has("continuationItemRenderer")) {
                items = getContinue(items.get(items.size() - 1).getAsJsonObject(), apiKey, context);
            } else {
                videos.add(parseFunc.apply(items.get(items.size() - 1).getAsJsonObject()));
                break;
            }
        }
        return videos;
    }

    static JsonObject parseContext(Document document) throws ResponseParsingException {
        assert document != null;
        Matcher mat;
        for (Element el : document.select("script")) {
            if ((mat = CONTEXT_REGEX.matcher(el.data())).find()) {
                JsonObject j = new Gson().fromJson(Objects.requireNonNull(mat.group(1)), JsonObject.class);
                JsonObject context = new JsonObject();
                context.add("context", j.getAsJsonObject("INNERTUBE_CONTEXT"));
                return context;
            }
        }
        throw new ResponseParsingException("API key not found");
    }

    static JsonArray getContinue(JsonObject lastObject, String apiKey, JsonObject context) throws IOException {
        assert lastObject != null;
        assert apiKey != null;
        assert context != null;
        JsonObject endP = lastObject
                .getAsJsonObject("continuationItemRenderer")
                .getAsJsonObject("continuationEndpoint");
        String token = endP
                .getAsJsonObject("continuationCommand")
                .get("token").getAsString();
        String apiUrl = endP.getAsJsonObject("commandMetadata")
                .getAsJsonObject("webCommandMetadata")
                .get("apiUrl").getAsString();
        context.addProperty("continuation", token);
        String page = NetTools.postApi(String.format(API_TEMPLATE, apiUrl, apiKey), context);
        return new Gson().fromJson(page, JsonObject.class)
                .getAsJsonArray("onResponseReceivedActions")
                .get(0).getAsJsonObject()
                .getAsJsonObject("appendContinuationItemsAction")
                .getAsJsonArray("continuationItems");
    }
}

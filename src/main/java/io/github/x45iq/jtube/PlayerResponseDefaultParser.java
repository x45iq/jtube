package io.github.x45iq.jtube;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PlayerResponseDefaultParser extends PlayerResponseParser {
    static final Pattern PLAYER_RESPONSE_REGEX = Pattern.compile("var\\s*ytInitialPlayerResponse\\s*=\\s*(\\{.*\\});");

    @Override
    JsonObject parse(Document document) throws ResponseParsingException {
        assert document != null;
        Matcher mat;
        for (Element el : document.select("script")) {
            if ((mat = PLAYER_RESPONSE_REGEX.matcher(el.data())).find()) {
                return JsonParser.parseString(mat.group(1)).getAsJsonObject();
            }
        }
        throw new ResponseParsingException("player response not found");
    }
}

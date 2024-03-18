package io.github.x45iq.jtube;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * The {@code ChannelParser} class represents tool to get {@code Channel} object by url.
 *
 * @author Artem Shein
 */
public final class ChannelParser {
    /**
     * Default constructor.
     */
    public ChannelParser() {

    }

    /**
     * Returns is url supported for parsing
     *
     * @param url channel url
     * @return {@code true} if the URL is supported, otherwise {@code false}
     */
    public static boolean isUrlSupported(String url) {
        return UrlPatternsTool.isChannelUrl(url);
    }

    private static ChannelMeta parseMeta(JsonObject mainJson) {
        JsonObject channelMetadataRenderer = mainJson
                .getAsJsonObject("metadata")
                .getAsJsonObject("channelMetadataRenderer");
        String title = channelMetadataRenderer.get("title").getAsString();
        String description = channelMetadataRenderer.has("description") ? channelMetadataRenderer.get("description").getAsString() : "";
        String thumbnail = channelMetadataRenderer
                .getAsJsonObject("avatar")
                .getAsJsonArray("thumbnails")
                .get(0)
                .getAsJsonObject()
                .get("url")
                .getAsString();
        String id = channelMetadataRenderer.get("vanityChannelUrl").getAsString();
        id = id.substring(id.lastIndexOf('@'));
        String externalId = channelMetadataRenderer.get("externalId").getAsString();
        return new ChannelMeta(title, description, id, externalId, thumbnail);
    }

    private static Channel parsePage(Document page) throws ResponseParsingException, IOException {
        JsonObject mainJson = ContainersApiTools.parseInitialData(page);

        ChannelMeta channelMeta = parseMeta(mainJson);

        JsonArray items = mainJson
                .getAsJsonObject("contents")
                .getAsJsonObject("twoColumnBrowseResultsRenderer")
                .getAsJsonArray("tabs")
                .get(1).getAsJsonObject()
                .getAsJsonObject("tabRenderer")
                .getAsJsonObject("content")
                .getAsJsonObject("richGridRenderer")
                .getAsJsonArray("contents");

        return new Channel(channelMeta, ContainersApiTools.parseVideos(page, items, json -> getChannelVideo(json, channelMeta.title())));
    }

    private static ContainerVideo getChannelVideo(JsonObject item, String owner) {
        JsonObject jsonVideo = item
                .getAsJsonObject("richItemRenderer")
                .getAsJsonObject("content")
                .getAsJsonObject("videoRenderer");
        String videoName = jsonVideo
                .getAsJsonObject("title")
                .getAsJsonArray("runs")
                .get(0).getAsJsonObject()
                .get("text").getAsString();
        String videoID = jsonVideo.get("videoId").getAsString();
        return new ContainerVideo(videoName, owner, videoID);
    }

    /**
     * Returns information about channel located at the given url
     *
     * @param url channel url
     * @return {@code Channel} object
     * @throws IOException              on Web error
     * @throws ResponseParsingException on error while parsing
     * @throws IllegalArgumentException on not supported url
     */
    public Channel parse(String url) throws IOException, ResponseParsingException, IllegalArgumentException {
        String id = UrlPatternsTool.getChannelUrlId(url).orElseThrow(() -> new IllegalArgumentException("Not supported url: " + url));

        Document page = NetTools.getPage(String.format("https://www.youtube.com/%s/videos", id));

        return parsePage(page);
    }
}

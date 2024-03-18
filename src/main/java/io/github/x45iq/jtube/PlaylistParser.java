package io.github.x45iq.jtube;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static io.github.x45iq.jtube.ContainersApiTools.parseInitialData;
import static io.github.x45iq.jtube.ContainersApiTools.parseVideos;

/**
 * The {@code PlaylistParser} class represents tool to get {@code Playlist} object by url.
 *
 * @author Artem Shein
 */
public final class PlaylistParser {
    /**
     * Default constructor.
     */
    public PlaylistParser() {

    }

    /**
     * Returns is url supported for parsing
     *
     * @param url playlist url
     * @return {@code true} if the URL is supported, otherwise {@code false}
     */
    public static boolean isUrlSupported(String url) {
        return UrlPatternsTool.isPlaylistUrl(url);
    }

    private static PlaylistMeta parseMeta(JsonObject mainJson) {
        assert mainJson != null;
        JsonObject playlistHeaderRenderer = mainJson
                .getAsJsonObject("header")
                .getAsJsonObject("playlistHeaderRenderer");
        String title = playlistHeaderRenderer
                .getAsJsonObject("title")
                .get("simpleText").getAsString();
        JsonObject descriptionField = playlistHeaderRenderer
                .getAsJsonObject("descriptionText");
        String id = playlistHeaderRenderer
                .get("playlistId")
                .getAsString();
        String owner = playlistHeaderRenderer
                .getAsJsonObject("ownerText")
                .getAsJsonArray("runs")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        String description = descriptionField.isEmpty() ? "" : descriptionField.get("simpleText").getAsString();

        return new PlaylistMeta(title, owner, description, id);
    }

    private static Playlist parsePage(Document page) throws IOException, ResponseParsingException {
        assert page != null;
        JsonObject mainJson = parseInitialData(page);

        PlaylistMeta playlistMeta = parseMeta(mainJson);

        JsonArray items = mainJson.getAsJsonObject("contents")
                .getAsJsonObject("twoColumnBrowseResultsRenderer")
                .getAsJsonArray("tabs")
                .get(0).getAsJsonObject()
                .getAsJsonObject("tabRenderer")
                .getAsJsonObject("content")
                .getAsJsonObject("sectionListRenderer")
                .getAsJsonArray("contents")
                .get(0).getAsJsonObject()
                .getAsJsonObject("itemSectionRenderer")
                .getAsJsonArray("contents")
                .get(0).getAsJsonObject()
                .getAsJsonObject("playlistVideoListRenderer")
                .getAsJsonArray("contents");

        return new Playlist(playlistMeta, parseVideos(page, items, PlaylistParser::getPlaylistVideo));
    }

    private static ContainerVideo getPlaylistVideo(JsonObject item) {
        assert item != null;
        JsonObject playlistVideoRenderer = item
                .getAsJsonObject("playlistVideoRenderer");
        String videoID = playlistVideoRenderer
                .get("videoId").getAsString();
        String videoName = playlistVideoRenderer
                .getAsJsonObject("title")
                .getAsJsonArray("runs")
                .get(0).getAsJsonObject()
                .get("text").getAsString();
        String ownerName = playlistVideoRenderer
                .getAsJsonObject("shortBylineText")
                .getAsJsonArray("runs")
                .get(0).getAsJsonObject()
                .get("text").getAsString();
        return new ContainerVideo(videoName, ownerName, videoID);
    }

    /**
     * Returns information about playlist located at the given url
     *
     * @param url playlist url
     * @return {@code Playlist} object
     * @throws IOException              on Web error
     * @throws ResponseParsingException on error while parsing
     * @throws IllegalArgumentException on not supported url
     */
    public Playlist parse(String url) throws IOException, ResponseParsingException, IllegalArgumentException {
        String id = UrlPatternsTool.getPlaylistUrlId(url).orElseThrow(() -> new IllegalArgumentException("Not supported url: " + url));
        Document page = NetTools.getPage(String.format("https://www.youtube.com/playlist?list=%s", id));
        return parsePage(page);
    }
}

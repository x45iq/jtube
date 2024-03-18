package io.github.x45iq.jtube;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class UrlPatternsTool {
    static final Pattern YOUTUBE_VIDEO_URL_REGEX = Pattern.compile("https://(www\\.|.{0})youtu(\\.be|be\\.com)/(shorts/|watch\\?v=|.{0})([0-9a-zA-Z_\\-]{11})");
    static final Pattern YOUTUBE_PLAYLIST_URL_REGEX = Pattern.compile("https://(www\\.|.{0})youtu(\\.be|be\\.com)/playlist\\?list=([0-9a-zA-Z_\\-]{34})");
    static final Pattern YOUTUBE_CHANNEL_OLD_URL_REGEX = Pattern.compile("https://(www\\.|.{0})youtu(\\.be|be\\.com)/channel/([0-9a-zA-Z_\\-]{24})");
    static final Pattern YOUTUBE_CHANNEL_URL_REGEX = Pattern.compile("https://(www\\.|.{0})youtu(\\.be|be\\.com)/@([0-9a-zA-Z_\\-.]{3,30})");

    static Optional<String> getVideoUrlId(String url) {
        Objects.requireNonNull(url, "url is null");

        Matcher youtubeMatch = YOUTUBE_VIDEO_URL_REGEX.matcher(url);

        String id = null;

        if (youtubeMatch.find()) id = youtubeMatch.group(4);
        return Optional.ofNullable(id);
    }

    static boolean isVideoUrl(String url) {
        Objects.requireNonNull(url, "url is null");
        return YOUTUBE_VIDEO_URL_REGEX.matcher(url).find();
    }

    static Optional<String> getPlaylistUrlId(String url) {
        Objects.requireNonNull(url, "url is null");
        Matcher youtubeMatch = YOUTUBE_PLAYLIST_URL_REGEX.matcher(url);

        String id = null;

        if (youtubeMatch.find())
            id = youtubeMatch.group(3);
        return Optional.ofNullable(id);
    }

    static boolean isPlaylistUrl(String url) {
        Objects.requireNonNull(url, "url is null");
        return YOUTUBE_PLAYLIST_URL_REGEX.matcher(url).find();
    }

    static Optional<String> getChannelUrlId(String url) {
        Objects.requireNonNull(url, "url is null");
        Matcher youtubeMatch = YOUTUBE_CHANNEL_URL_REGEX.matcher(url);
        Matcher youtubeOldMatch = YOUTUBE_CHANNEL_OLD_URL_REGEX.matcher(url);


        String id = null;

        if (youtubeMatch.find()) {
            id = String.format("@%s", youtubeMatch.group(3));
        } else if (youtubeOldMatch.find()) {
            id = youtubeOldMatch.group(3);
        }
        return Optional.ofNullable(id);
    }

    static boolean isChannelUrl(String url) {
        Objects.requireNonNull(url, "url is null");
        return YOUTUBE_CHANNEL_URL_REGEX.matcher(url).find() || YOUTUBE_CHANNEL_OLD_URL_REGEX.matcher(url).find();
    }

}

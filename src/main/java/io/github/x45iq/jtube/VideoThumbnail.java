package io.github.x45iq.jtube;

import java.io.Serializable;

/**
 * The {@code VideoThumbnail} class represents YouTube video thumbnail.
 *
 * @author Artem Shein
 */
public final class VideoThumbnail implements Serializable {
    private static final String IMAGE_BASE_URL = "https://i.ytimg.com/vi/";

    private final String id;

    VideoThumbnail(String id) {
        assert id != null;
        this.id = id;
    }

    /**
     * Returns the video thumbnail with default quality.
     *
     * @return {@code String} url
     */
    public String defaultImage() {
        return String.format("%s%s/default.jpg", IMAGE_BASE_URL, id);
    }

    /**
     * Returns the video thumbnail with medium quality.
     *
     * @return {@code String} url
     */
    public String mqImage() {
        return String.format("%s%s/mqdefault.jpg", IMAGE_BASE_URL, id);
    }

    /**
     * Returns the video thumbnail with high quality.
     *
     * @return {@code String} url
     */
    public String hqImage() {
        return String.format("%s%s/hqdefault.jpg", IMAGE_BASE_URL, id);
    }

    /**
     * Returns the video thumbnail with SD quality.
     *
     * @return {@code String} url
     */
    public String sdImage() {
        return String.format("%s%s/sddefault.jpg", IMAGE_BASE_URL, id);
    }

    /**
     * Returns the video thumbnail with maximum quality.
     *
     * @return {@code String} url
     */
    public String maxResImage() {
        return String.format("%s%s/maxresdefault.jpg", IMAGE_BASE_URL, id);
    }
}

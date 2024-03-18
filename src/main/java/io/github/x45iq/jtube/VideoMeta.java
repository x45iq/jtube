package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code VideoMeta} class represents information about YouTube video.
 *
 * @author Artem Shein
 */
public final class VideoMeta implements Serializable {
    private final String title;
    private final String author;
    private final String id;
    private final String description;

    VideoMeta(String title, String author, String id, String description) {
        assert title != null;
        assert author != null;
        assert id != null;
        assert description != null;
        this.title = title;
        this.author = author;
        this.id = id;
        this.description = description;
    }

    /**
     * Returns the information about thumbnail.
     *
     * @return {@code VideoThumbnail} object
     */
    public VideoThumbnail thumbnail() {
        return new VideoThumbnail(id);
    }

    /**
     * Returns the video title.
     *
     * @return {@code String} value
     */
    public String title() {
        return title;
    }

    /**
     * Returns the video author.
     *
     * @return {@code String} value
     */
    public String author() {
        return author;
    }

    /**
     * Returns the video id.
     *
     * @return {@code String} value
     */
    public String id() {
        return id;
    }

    /**
     * Returns the description of the video
     *
     * @return {@code String} value
     */
    public String description() {
        return description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, id, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoMeta)) return false;
        VideoMeta that = (VideoMeta) o;
        return Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public String toString() {
        return "Meta{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}

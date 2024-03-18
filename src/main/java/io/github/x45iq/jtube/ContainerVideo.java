package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code ContainerVideo} class represents pure information about YouTube video.
 *
 * @author Artem Shein
 */
public final class ContainerVideo implements Serializable {
    static final String LINK_TEMPLATE = "https://www.youtube.com/watch?v=%s";
    private final String title;
    private final String author;
    private final String id;

    /**
     * Allocates a new {@code ContainerVideo}
     *
     * @param title  video title
     * @param author video author
     * @param id     video id
     */
    ContainerVideo(String title, String author, String id) {
        assert title != null;
        assert author != null;
        assert id != null;
        this.title = title;
        this.author = author;
        this.id = id;
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
     * Returns the video url.
     *
     * @return {@code String} value
     */
    public String url() {
        return String.format(LINK_TEMPLATE, id);
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
     * Returns the video id.
     *
     * @return {@code String} value
     */
    public String id() {
        return id;
    }

    /**
     * Returns the video author.
     *
     * @return {@code String} value
     */
    public String author() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerVideo that = (ContainerVideo) o;
        return Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, id);
    }

    @Override
    public String toString() {
        return "ContainerVideo{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

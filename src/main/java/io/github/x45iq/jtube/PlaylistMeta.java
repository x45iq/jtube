package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code PlaylistMeta} class represents information about YouTube playlist.
 *
 * @author Artem Shein
 */
public final class PlaylistMeta implements Serializable {
    private final String title;
    private final String author;
    private final String description;
    private final String id;

    PlaylistMeta(String title, String author, String description, String id) {
        assert title != null;
        assert author != null;
        assert description != null;
        assert id != null;
        this.title = title;
        this.author = author;
        this.description = description;
        this.id = id;
    }

    /**
     * Returns the title of the playlist
     *
     * @return {@code String} value
     */
    public String title() {
        return title;
    }

    /**
     * Returns the author of the playlist
     *
     * @return {@code String} value
     */
    public String author() {
        return author;
    }

    /**
     * Returns the description of the playlist
     *
     * @return {@code String} value
     */
    public String description() {
        return description;
    }

    /**
     * Returns the id of the playlist
     *
     * @return {@code String} value
     */
    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistMeta that = (PlaylistMeta) o;
        return Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(description, that.description) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, description, id);
    }

    @Override
    public String toString() {
        return "PlaylistMeta{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

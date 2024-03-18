package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * The {@code Playlist} class represents YouTube playlist.
 *
 * @author Artem Shein
 */
public final class Playlist implements Serializable {
    private final PlaylistMeta meta;
    private final List<ContainerVideo> videos;

    Playlist(PlaylistMeta meta, List<ContainerVideo> videos) {
        assert meta != null;
        assert videos != null;
        this.meta = meta;
        this.videos = videos;
    }

    /**
     * Returns the information about playlist.
     *
     * @return {@code PlaylistMeta} value
     */
    public PlaylistMeta meta() {
        return meta;
    }

    /**
     * Returns videos saved in playlist.
     *
     * @return {@code List<ContainerVideo>} value
     */
    public List<ContainerVideo> videos() {
        return videos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, videos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist)) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(meta, playlist.meta) && Objects.equals(videos, playlist.videos);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "meta=" + meta +
                ", videos=" + videos +
                '}';
    }

}

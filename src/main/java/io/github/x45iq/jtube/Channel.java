package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * The {@code Channel} class represents YouTube channel.
 *
 * @author Artem Shein
 */
public final class Channel implements Serializable {
    private final ChannelMeta meta;
    private final List<ContainerVideo> videos;

    /**
     * Allocates a new {@code Channel}
     *
     * @param meta   channel meta
     * @param videos channel videos
     */
    Channel(ChannelMeta meta, List<ContainerVideo> videos) {
        assert meta != null;
        assert videos != null;
        this.meta = meta;
        this.videos = videos;
    }

    /**
     * Returns the information about channel.
     *
     * @return {@code ChannelMeta} value
     */
    public ChannelMeta meta() {
        return meta;
    }

    /**
     * Returns videos uploaded on channel.
     *
     * @return {@code List<ContainerVideo>} value
     */
    public List<ContainerVideo> videos() {
        return videos;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Channel)) return false;
        Channel channel = (Channel) o;
        return Objects.equals(meta, channel.meta) && Objects.equals(videos, channel.videos);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "meta=" + meta +
                ", videos=" + videos +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, videos);
    }

}

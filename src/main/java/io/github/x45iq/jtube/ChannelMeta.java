package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code ChannelMeta} class represents information about YouTube channel.
 *
 * @author Artem Shein
 */
public final class ChannelMeta implements Serializable {
    private final String title;
    private final String description;
    private final String id;
    private final String externalId;
    private final String thumbnailUrl;

    /**
     * Allocates a new {@code ChannelMeta}
     *
     * @param title        channel title
     * @param description  channel description
     * @param id           channel short id (@jtubechannel)
     * @param externalId   channel long id (UCuAXF...)
     * @param thumbnailUrl channel thumbnail url
     */
    ChannelMeta(String title, String description, String id, String externalId, String thumbnailUrl) {
        assert title != null;
        assert description != null;
        assert id != null;
        assert externalId != null;
        assert thumbnailUrl != null;
        this.title = title;
        this.description = description;
        this.id = id;
        this.externalId = externalId;
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * Returns the title of the channel
     *
     * @return {@code String} value
     */
    public String title() {
        return title;
    }

    /**
     * Returns the description of the channel
     *
     * @return {@code String} value
     */
    public String description() {
        return description;
    }

    /**
     * Returns the short channel id (@jtubechannel)
     *
     * @return {@code String} value
     */
    public String id() {
        return id;
    }

    /**
     * Returns the long channel id (@UCuAXF...)
     *
     * @return {@code String} value
     */
    public String externalId() {
        return externalId;
    }

    /**
     * Returns the url of channel thumbnail
     *
     * @return {@code String} value
     */
    public String thumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelMeta that = (ChannelMeta) o;
        return Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(id, that.id) && Objects.equals(externalId, that.externalId) && Objects.equals(thumbnailUrl, that.thumbnailUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, externalId, thumbnailUrl);
    }

    @Override
    public String toString() {
        return "ChannelMeta{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", externalId='" + externalId + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}

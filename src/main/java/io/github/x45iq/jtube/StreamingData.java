package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code StreamingData} class represents information about streaming data.
 *
 * @author Artem Shein
 */
public abstract class StreamingData implements Serializable {
    private final String url;
    private final long contentLength;

    StreamingData(String url, long contentLength) {
        assert url != null;
        assert contentLength > 0;
        this.url = url;
        this.contentLength = contentLength;
    }

    /**
     * Returns the streaming data url.
     *
     * @return {@code String} url
     */
    public String url() {
        return url;
    }

    /**
     * Returns the streaming size in bytes.
     *
     * @return {@code long} size
     */
    public long contentLength() {
        return contentLength;
    }

    /**
     * Returns the streaming data representing the format as a {@code String}.
     *
     * @return {@code String} format
     */
    public abstract String format();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamingData that = (StreamingData) o;
        return contentLength == that.contentLength && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, contentLength);
    }

    @Override
    public String toString() {
        return "StreamingData{" +
                "url='" + url + '\'' +
                ", contentLength=" + contentLength +
                '}';
    }
}

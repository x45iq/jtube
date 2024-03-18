package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code VideoResolution} class represents information about {@code StreamingData} resolution.
 *
 * @author Artem Shein
 */
public final class VideoResolution implements Serializable {
    private final int width;
    private final int height;
    private final VideoQuality quality;

    VideoResolution(int width, int height, VideoQuality quality) {
        assert width > 0;
        assert height > 0;
        assert quality != null;
        this.width = width;
        this.height = height;
        this.quality = quality;
    }

    /**
     * Returns the video width.
     *
     * @return {@code int} value
     */
    public int width() {
        return width;
    }

    /**
     * Returns the video height.
     *
     * @return {@code int} value
     */
    public int height() {
        return height;
    }

    /**
     * Returns the video quality.
     *
     * @return {@code VideoQuality} object
     */
    public VideoQuality quality() {
        return quality;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        VideoResolution that = (VideoResolution) obj;
        return this.width == that.width &&
                this.height == that.height &&
                Objects.equals(this.quality, that.quality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, quality);
    }

    @Override
    public String toString() {
        return "VideoResolution[" +
                "width=" + width + ", " +
                "height=" + height + ", " +
                "quality=" + quality + ']';
    }

}

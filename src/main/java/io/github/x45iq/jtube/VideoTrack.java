package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code VideoTrack} class represents information about video track.
 *
 * @author Artem Shein
 */
public final class VideoTrack implements Serializable {

    private final VideoResolution resolution;
    private final int fps;
    private final VideoFormat format;
    private final String codec;

    VideoTrack(VideoResolution resolution, int fps, VideoFormat format, String codec) {
        assert format != null;
        assert fps > 0;
        assert codec != null;
        this.format = format;
        this.codec = codec;
        this.resolution = resolution;
        this.fps = fps;
    }

    /**
     * Returns the video format.
     *
     * @return {@code VideoFormat} value
     */
    public VideoFormat format() {
        return format;
    }

    /**
     * Returns the video codec.
     *
     * @return {@code String} value
     */
    public String codec() {
        return codec;
    }

    /**
     * Returns the video resolution.
     *
     * @return {@code VideoResolution} resolution
     */
    public VideoResolution resolution() {
        return resolution;
    }

    /**
     * Returns the video fps.
     *
     * @return {@code int} fps
     */
    public int fps() {
        return fps;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        VideoTrack that = (VideoTrack) obj;
        return Objects.equals(this.format, that.format) &&
                Objects.equals(this.codec, that.codec) &&
                Objects.equals(this.resolution, that.resolution) &&
                this.fps == that.fps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(format, codec, resolution, fps);
    }

    @Override
    public String toString() {
        return "VideoTrack[" +
                "format=" + format + ", " +
                "codec=" + codec + ", " +
                "resolution=" + resolution + ", " +
                "fps=" + fps + ']';
    }


}

package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code VideoStreamingData} class represents {@code StreamingData} class, with only one {@code VideoTrack}.
 *
 * @author Artem Shein
 */
public final class VideoStreamingData extends StreamingData implements Serializable {
    private final VideoTrack videoTrack;

    VideoStreamingData(String url, long contentLength, VideoTrack videoTrack) {
        super(url, contentLength);
        assert videoTrack != null;
        this.videoTrack = videoTrack;
    }

    /**
     * Returns the video track.
     *
     * @return {@code VideoTrack} value
     */
    public VideoTrack videoTrack() {
        return videoTrack;
    }

    @Override
    public String format() {
        return videoTrack.format().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VideoStreamingData that = (VideoStreamingData) o;
        return Objects.equals(videoTrack, that.videoTrack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), videoTrack);
    }

    @Override
    public String toString() {
        return "VideoStreamingData{" +
                "videoTrack=" + videoTrack +
                "} " + super.toString();
    }
}

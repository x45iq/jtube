package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code HybridStreamingData} class represents {@code StreamingData} class, with {@code AudioTrack} and {@code VideoTrack}.
 *
 * @author Artem Shein
 */
public final class HybridStreamingData extends StreamingData implements Serializable {
    private final VideoTrack videoTrack;
    private final PureAudioTrack audioTrack;

    /**
     * Allocates a new {@code HybridStreamingData}
     *
     * @param url           streaming data web url
     * @param contentLength streaming data size in bytes
     * @param videoTrack    streaming data video track
     * @param audioTrack    streaming data audio track
     */
    HybridStreamingData(String url, long contentLength, VideoTrack videoTrack, PureAudioTrack audioTrack) {
        super(url, contentLength);
        assert videoTrack != null;
        assert audioTrack != null;
        this.videoTrack = videoTrack;
        this.audioTrack = audioTrack;
    }

    /**
     * Returns the audio track.
     *
     * @return {@code AudioTrack} value
     */
    public PureAudioTrack audioTrack() {
        return audioTrack;
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
        HybridStreamingData that = (HybridStreamingData) o;
        return Objects.equals(videoTrack, that.videoTrack) && Objects.equals(audioTrack, that.audioTrack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), videoTrack, audioTrack);
    }

    @Override
    public String toString() {
        return "HybridStreamingData{" +
                "videoTrack=" + videoTrack +
                ", audioTrack=" + audioTrack +
                "} " + super.toString();
    }
}

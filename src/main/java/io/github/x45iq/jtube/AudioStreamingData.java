package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code AudioStreamingData} class represents {@code StreamingData} class, with only one {@code AudioTrack}.
 *
 * @author Artem Shein
 */
public final class AudioStreamingData extends StreamingData implements Serializable {

    private final AudioTrack audioTrack;

    /**
     * Allocates a new {@code AudioStreamingData}
     *
     * @param url           streaming data web url
     * @param contentLength streaming data size in bytes
     * @param audioTrack    streaming data audio track
     */
    AudioStreamingData(String url, long contentLength, AudioTrack audioTrack) {
        super(url, contentLength);
        assert audioTrack != null;
        this.audioTrack = audioTrack;
    }

    /**
     * Returns the audio track.
     *
     * @return {@code AudioTrack} value
     */
    public AudioTrack audioTrack() {
        return audioTrack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AudioStreamingData)) return false;
        if (!super.equals(o)) return false;
        AudioStreamingData that = (AudioStreamingData) o;
        return Objects.equals(audioTrack, that.audioTrack);
    }

    @Override
    public String format() {
        return audioTrack.format().toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), audioTrack);
    }

    @Override
    public String toString() {
        return "AudioStreamingData{" +
                "audioTrack=" + audioTrack +
                "} " + super.toString();
    }
}

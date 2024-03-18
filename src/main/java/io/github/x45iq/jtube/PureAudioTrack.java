package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code PureAudioTrack} class represents pure information about audio track.
 *
 * @author Artem Shein
 */
public final class PureAudioTrack implements Serializable {
    private final String codec;
    private final int sampleRate;

    PureAudioTrack(String codec, int sampleRate) {
        assert codec != null;
        assert sampleRate > 0;
        this.codec = codec;
        this.sampleRate = sampleRate;
    }

    /**
     * Returns the audio codec.
     *
     * @return {@code String} value
     */
    public String codec() {
        return codec;
    }

    /**
     * Returns the audio sampleRate.
     *
     * @return {@code int} value in Hz
     */
    public int sampleRate() {
        return sampleRate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        PureAudioTrack that = (PureAudioTrack) obj;
        return Objects.equals(this.codec, that.codec) &&
                this.sampleRate == that.sampleRate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codec, sampleRate);
    }

    @Override
    public String toString() {
        return "PureAudioTrack{" +
                "codec='" + codec + '\'' +
                ", sampleRate=" + sampleRate +
                '}';
    }
}

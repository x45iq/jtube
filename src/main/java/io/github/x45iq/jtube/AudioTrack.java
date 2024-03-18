package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code AudioTrack} class represents information about audio track.
 *
 * @author Artem Shein
 */
public final class AudioTrack implements Serializable {
    private final int bitrate;
    private final int sampleRate;
    private final Language language;
    private final AudioFormat format;
    private final String codec;

    /**
     * Allocates a new {@code AudioTrack}
     *
     * @param bitrate    audio bitrate in bits per second
     * @param sampleRate audio sampleRate in Hz
     * @param language   audio track language (might be null)
     * @param format     audio format
     * @param codec      audio codec
     */
    AudioTrack(int bitrate, int sampleRate, Language language, AudioFormat format, String codec) {
        assert bitrate > 0;
        assert sampleRate > 0;
        assert format != null;
        assert codec != null;
        this.format = format;
        this.codec = codec;
        this.bitrate = bitrate;
        this.sampleRate = sampleRate;
        this.language = language;
    }

    /**
     * Returns the audio format.
     *
     * @return {@code AudioFormat} value
     */
    public AudioFormat format() {
        return format;
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
     * Returns the audio bitrate.
     *
     * @return {@code int} value in bpr
     */
    public int bitrate() {
        return bitrate;
    }

    /**
     * Returns the audio sampleRate.
     *
     * @return {@code int} value in Hz
     */
    public int sampleRate() {
        return sampleRate;
    }

    /**
     * Returns the audio language.
     *
     * @return {@code Language} value or null if Language is default
     */
    public Language language() {
        return language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioTrack that = (AudioTrack) o;
        return bitrate == that.bitrate && sampleRate == that.sampleRate && format == that.format && Objects.equals(codec, that.codec) && Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format, codec, bitrate, sampleRate, language);
    }

    @Override
    public String toString() {
        return "AudioTrack{" +
                "bitrate=" + bitrate +
                ", sampleRate=" + sampleRate +
                ", language=" + language +
                ", format=" + format +
                ", codec='" + codec + '\'' +
                '}';
    }
}

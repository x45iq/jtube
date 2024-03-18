package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * The {@code Video} class represents YouTube video or shorts.
 *
 * @author Artem Shein
 */
public final class Video implements Serializable {
    private final VideoMeta meta;
    private final List<VideoStreamingData> videoStreamingData;
    private final List<AudioStreamingData> audioStreamingData;
    private final List<HybridStreamingData> hybridStreamingData;
    private final List<Subtitles> subtitles;

    Video(VideoMeta meta, List<VideoStreamingData> videoStreamingData, List<AudioStreamingData> audioStreamingData, List<HybridStreamingData> hybridStreamingData, List<Subtitles> subtitles) {
        assert meta != null;
        assert videoStreamingData != null;
        assert audioStreamingData != null;
        assert hybridStreamingData != null;
        assert subtitles != null;
        this.meta = meta;
        this.videoStreamingData = videoStreamingData;
        this.audioStreamingData = audioStreamingData;
        this.hybridStreamingData = hybridStreamingData;
        this.subtitles = subtitles;
    }

    /**
     * Returns the information about video.
     *
     * @return {@code VideoMeta} meta
     */
    public VideoMeta meta() {
        return meta;
    }

    /**
     * Returns the list of {@code VideoStreamingData}.
     *
     * @return {@code List<VideoStreamingData>} list
     */
    public List<VideoStreamingData> videoStreamingData() {
        return videoStreamingData;
    }

    /**
     * Returns the list of {@code AudioStreamingData}.
     *
     * @return {@code List<AudioStreamingData>} list
     */
    public List<AudioStreamingData> audioStreamingData() {
        return audioStreamingData;
    }

    /**
     * Returns the list of {@code HybridStreamingData}.
     *
     * @return {@code List<HybridStreamingData>} list
     */
    public List<HybridStreamingData> hybridStreamingData() {
        return hybridStreamingData;
    }

    /**
     * Returns the list of {@code Subtitles}.
     *
     * @return {@code List<Subtitles>} list
     */
    public List<Subtitles> subtitles() {
        return subtitles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(meta, video.meta) && Objects.equals(videoStreamingData, video.videoStreamingData) && Objects.equals(audioStreamingData, video.audioStreamingData) && Objects.equals(hybridStreamingData, video.hybridStreamingData) && Objects.equals(subtitles, video.subtitles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, videoStreamingData, audioStreamingData, hybridStreamingData, subtitles);
    }

    @Override
    public String toString() {
        return "Video{" +
                "meta=" + meta +
                ", videoStreamingData=" + videoStreamingData +
                ", audioStreamingData=" + audioStreamingData +
                ", hybridStreamingData=" + hybridStreamingData +
                ", subtitles=" + subtitles +
                '}';
    }

}

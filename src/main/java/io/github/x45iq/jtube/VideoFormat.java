package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * The {@code VideoFormat} class represents video file format.
 *
 * @author Artem Shein
 */
public enum VideoFormat implements Serializable {
    /**
     * Webm format
     */
    F_WEBM("webm"),
    /**
     * Mp4 format
     */
    F_MP4("mp4");
    private static final Map<String, VideoFormat> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));

    private final String string;

    VideoFormat(String string) {
        this.string = string;
    }

    /**
     * Returns the {@code VideoFormat} equivalent of a {@code String} value.
     *
     * @param value String value of VideoFormat
     * @return an {@code Optional} describing the {@code VideoFormat} equivalent of a value,
     * or an empty {@code Optional}, if there is no such
     */
    public static Optional<VideoFormat> fromString(String value) {
        return Optional.ofNullable(stringToEnum.get(value));
    }

    @Override
    public String toString() {
        return string;
    }
}

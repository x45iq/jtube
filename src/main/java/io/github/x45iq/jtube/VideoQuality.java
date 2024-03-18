package io.github.x45iq.jtube;


import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * The {@code VideoQuality} class represents video quality.
 *
 * @author Artem Shein
 */
public enum VideoQuality {
    Q_144p(144),
    Q_240p(240),
    Q_360p(360),
    Q_480p(480),
    Q_720p(720),
    Q_1080p(1080),
    Q_1440p(1440),
    Q_2160p(2160),
    Q_4320p(4320);
    private static final Map<String, VideoQuality> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));
    private final int value;

    VideoQuality(int value) {
        this.value = value;
    }

    /**
     * Returns the {@code VideoQuality} equivalent of a {@code String} value.
     *
     * @param value String value of VideoQuality
     * @return an {@code Optional} describing the {@code VideoQuality} equivalent of a value,
     * or an empty {@code Optional}, if there is no such
     */
    public static Optional<VideoQuality> fromString(String value) {
        return Optional.ofNullable(stringToEnum.get(value));
    }

    /**
     * Returns int value of quality
     *
     * @return {@code int} value
     */
    public int intValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%sp", value);
    }
}

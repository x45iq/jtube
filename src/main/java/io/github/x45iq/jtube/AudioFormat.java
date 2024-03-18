package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.*;

/**
 * The {@code AudioFormat} class represents audio file format.
 *
 * @author Artem Shein
 */
public enum AudioFormat implements Serializable {
    /**
     * Opus format
     */
    F_OPUS(Arrays.asList("opus", "webm")),
    /**
     * M4A format
     */
    F_M4A(Arrays.asList("m4a", "mp4"));
    private static final Map<String, AudioFormat> stringToEnum = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(format -> format.values.forEach(str -> stringToEnum.put(str, format)));
    }

    private final List<String> values;

    AudioFormat(List<String> values) {
        this.values = values;
    }

    /**
     * Returns the {@code AudioFormat} equivalent of a {@code String} value.
     *
     * @param value String value of AudioFormat
     * @return an {@code Optional} describing the {@code AudioFormat} equivalent of a value,
     * or an empty {@code Optional}, if there is no such
     */
    public static Optional<AudioFormat> fromString(String value) {
        return Optional.ofNullable(stringToEnum.get(value));
    }

    @Override
    public String toString() {
        return values.get(0);
    }
}

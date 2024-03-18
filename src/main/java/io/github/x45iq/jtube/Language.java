package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Language} class represents human language.
 *
 * @author Artem Shein
 */
public final class Language implements Serializable {
    private final String code;
    private final String name;

    Language(String code, String name) {
        assert code != null;
        assert name != null;
        this.code = code;
        this.name = name;
    }

    /**
     * Returns language code.
     *
     * @return {@code String} code value
     */
    public String code() {
        return code;
    }

    /**
     * Returns language name.
     *
     * @return {@code String} language name
     */
    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return Objects.equals(code, language.code) && Objects.equals(name, language.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }

    @Override
    public String toString() {
        return "Language{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

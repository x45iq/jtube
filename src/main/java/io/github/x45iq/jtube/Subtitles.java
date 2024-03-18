package io.github.x45iq.jtube;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Subtitles} class represents YouTube video subtitles in xml format.
 *
 * @author Artem Shein
 */
public final class Subtitles implements Serializable {
    private final String url;
    private final Language language;

    Subtitles(String url, Language language) {
        assert url != null;
        assert language != null;
        this.url = url;
        this.language = language;
    }

    /**
     * Returns the subtitles url.
     *
     * @return {@code String} url
     */
    public String url() {
        return url;
    }

    /**
     * Returns the subtitles language.
     *
     * @return {@code Language} language
     */
    public Language language() {
        return language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtitles subtitles = (Subtitles) o;
        return Objects.equals(url, subtitles.url) && Objects.equals(language, subtitles.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, language);
    }

    @Override
    public String toString() {
        return "Subtitles{" +
                "url='" + url + '\'' +
                ", language=" + language +
                '}';
    }
}

package io.github.x45iq.jtube;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code CacheData} class represents information for caching.
 *
 * @author Artem Shein
 */
public final class CacheData implements Serializable {
    private final File cacheFolder;

    /**
     * Allocates a new {@code CacheData}
     *
     * @param cacheFolder folder for caching
     */
    public CacheData(File cacheFolder) {
        this.cacheFolder = Objects.requireNonNull(cacheFolder);
        if(!cacheFolder.isDirectory())throw new IllegalArgumentException("Must be dir");
    }

    File cacheFolder() {
        return cacheFolder;
    }
}

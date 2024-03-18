package io.github.x45iq.jtube;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

class CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);

    private CacheManager() {

    }

    static <T> void cacheObject(CacheData cacheData, String fileName, T object) {
        assert fileName != null;
        assert object != null;
        if (cacheData == null) {
            return;
        }
        File cacheFile = new File(cacheData.cacheFolder(), fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile))) {
            writer.write(new Gson().toJson(object));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    static <T> Optional<T> getCacheObject(CacheData cacheData, String fileName, Class<T> clazz) {
        assert fileName != null;
        assert clazz != null;
        if (cacheData == null) {
            return Optional.empty();
        }
        File cacheFile = new File(cacheData.cacheFolder(), fileName);
        if (cacheFile.exists()) {
            try {
                String json = String.join("\n", Files.readAllLines(cacheFile.toPath()));
                return Optional.ofNullable(new Gson().fromJson(json, clazz));
            } catch (IOException e) {
                logger.error("", e);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    static void deleteFromCache(CacheData cacheData, String fileName) {
        assert fileName != null;
        if (cacheData == null) {
            return;
        }
        File cacheFile = new File(cacheData.cacheFolder(), fileName);
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
    }
}

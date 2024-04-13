package io.github.x45iq.jtube;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;
import java.util.function.LongConsumer;

class PacketDownloader implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PacketDownloader.class);
    private final String url;
    private final long start;
    private final long end;
    private final File file;
    private final LongConsumer callback;
    private final Runnable failCallback;

    PacketDownloader(String url, File file, long start, long end, LongConsumer callback, Runnable failCallback) {
        assert url != null;
        assert file != null;
        assert start >= 0;
        assert end > 0;
        assert callback != null;
        assert failCallback != null;
        this.end = end;
        this.start = start;
        this.file = file;
        this.url = url;
        this.callback = callback;
        this.failCallback = failCallback;
    }

    @Override
    public void run() {
        while (true) {
            try {
                download();
                return;
            } catch (IOException e) {
                logger.error("", e);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    return;
                }
            } catch (Throwable e) {
                failCallback.run();
                return;
            }
        }
    }

    private void download() throws IOException {
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "rwd")) {
            accessFile.seek(start);
            accessFile.write(Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .userAgent(RandomUserAgent.create())
                    .header("Range", String.format("bytes=%s-%s", start, end))
                    .requestBody("x\u0000")
                    .execute()
                    .bodyAsBytes());
            callback.accept(end - start);
        }
    }

}

package io.github.x45iq.jtube;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static io.github.x45iq.jtube.FileTools.*;

/**
 * The {@code StreamingDataDownloader} class represents tool to download {@code StreamingData} object.
 *
 * @author Artem Shein
 */
public final class StreamingDataDownloader {
    private final StreamingData streamingData;
    private final File folder;
    private final String fileName;
    private final int threadsCount;
    private final long packetSize;
    private final int callbackTimeoutMils;
    private final Consumer<Progress> progressCallback;

    private StreamingDataDownloader(Builder builder) {
        this.streamingData = builder.streamingData;
        this.folder = builder.folder;
        this.fileName = builder.fileName;
        this.threadsCount = builder.threadsCount;
        this.packetSize = builder.packetSize;
        this.callbackTimeoutMils = builder.callbackTimeoutMils;
        this.progressCallback = builder.progressCallback;
    }

    /**
     * Downloads {@code StreamingData} in a format according to the abstract method {@code format()}
     *
     * @return {@code File} object to which {@code StreamingData} was downloaded
     * @throws IOException on error
     */
    public File download() throws IOException {
        final File exportFile = createExportFile(folder, fileName, streamingData.format());
        final ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        final long len = streamingData.contentLength();
        final AtomicLong downloaded = new AtomicLong(0);
        long used = 0;
        final AtomicBoolean failFlag = new AtomicBoolean(false);
        Runnable failCallback = () -> failFlag.set(true);
        while (used < len) {
            long start = used;
            long end;
            if (start + packetSize > len) {
                end = len;
            } else {
                end = used + packetSize;
            }
            executorService.submit(new PacketDownloader(streamingData.url(), exportFile, start, end, downloaded::addAndGet, failCallback));
            used = end;
        }
        try {
            exportFile.createNewFile();
            executorService.shutdown();
            while (!executorService.isTerminated() && !failFlag.get()) {
                progressCallback.accept(new Progress(downloaded.get(), len));
                try {
                    Thread.sleep(callbackTimeoutMils);
                } catch (InterruptedException e) {
                    failFlag.set(true);
                    throw new IOException(e);
                }
            }
            if (failFlag.get()) {
                throw new IOException();
            } else {
                return exportFile;
            }
        } finally {
            if (!executorService.isTerminated()) {
                executorService.shutdownNow();
            }
            if (failFlag.get()) {
                deleteFile(exportFile);
            }
        }
    }

    /**
     * The {@code StreamingDataDownloader.Builder} class represents a builder for StreamingDataDownloader.
     */
    public static final class Builder implements Cloneable {
        private StreamingData streamingData = null;
        private File folder = null;
        private String fileName = null;
        private int threadsCount = 40;
        private long packetSize = 1024*100;//100kb
        private int callbackTimeoutMils = 1000;
        private Consumer<Progress> progressCallback = null;

        /**
         * Sets progress callback timeout in mils
         *
         * @param callbackTimeoutMils timeout
         * @return {@code Builder}
         */
        public Builder callbackTimeout(int callbackTimeoutMils) {
            this.callbackTimeoutMils = callbackTimeoutMils;
            return this;
        }

        /**
         * Sets parent folder for export file
         *
         * @param folder directory
         * @return {@code Builder}
         */
        public Builder folder(File folder) {
            this.folder = folder;
            return this;
        }
        /**
         * Sets download packet size
         *
         * @param packetSize size
         * @return {@code Builder}
         */
        public Builder packetSize(long packetSize) {
            this.packetSize = packetSize;
            return this;
        }


        /**
         * Sets progress callback
         *
         * @param progressCallback callback
         * @return {@code Builder}
         */
        public Builder progressCallback(Consumer<Progress> progressCallback) {
            this.progressCallback = progressCallback;
            return this;
        }

        /**
         * Sets filename for export file
         *
         * @param fileName name
         * @return {@code Builder}
         */
        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        /**
         * Sets downloading thread count
         *
         * @param threadsCount count
         * @return {@code Builder}
         */
        public Builder threadsCount(int threadsCount) {
            this.threadsCount = threadsCount;
            return this;
        }

        /**
         * Sets streaming data
         *
         * @param streamingData data
         * @return {@code Builder}
         */
        public Builder streamingData(StreamingData streamingData) {
            this.streamingData = streamingData;
            return this;
        }

        /**
         * Returns {@code StreamingDataDownloader} with {@code Builder} params
         *
         * @return {@code StreamingDataDownloader} object;
         */
        public StreamingDataDownloader build() {
            Objects.requireNonNull(streamingData);
            Objects.requireNonNull(folder);
            fileName = fileName == null ? createRandomFileName() : fileName;
            if (threadsCount <= 0) throw new IndexOutOfBoundsException("n > 0");
            if (packetSize <= 0) throw new IndexOutOfBoundsException("n > 0");
            if (callbackTimeoutMils < 0) throw new IndexOutOfBoundsException("n >= 0");
            try {
                return new StreamingDataDownloader((Builder) this.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

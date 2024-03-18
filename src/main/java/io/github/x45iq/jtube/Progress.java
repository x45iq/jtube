package io.github.x45iq.jtube;

import java.io.Serializable;
/**
 * The {@code Progress} class represents downloading progress.
 * @author Artem Shein
 */
public final class Progress implements Serializable {
    private final long downloaded;
    private final long contentLen;

    Progress(long downloaded, long contentLen) {
        assert downloaded >= 0;
        assert contentLen >= 0;
        this.downloaded = downloaded;
        this.contentLen = contentLen;
    }
    /**
     * Returns the percentage downloaded to contentLen
     * @return {@code float} value
     */
    public float percentValue() {
        if (contentLen == 0) {
            return 0;
        } else {
            return (float) (downloaded * 100.0 / contentLen);
        }
    }
    /**
     * Returns downloaded in bytes
     * @return {@code long} value
     */
    public long downloaded() {
        return downloaded;
    }
    /**
     * Returns contentLen in bytes
     * @return {@code long} value
     */
    public long contentLen() {
        return contentLen;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "downloaded=" + downloaded +
                ", contentLen=" + contentLen +
                ", percent=" + percentValue() +
                '}';
    }
}

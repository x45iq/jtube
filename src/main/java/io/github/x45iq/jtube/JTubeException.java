package io.github.x45iq.jtube;

class JTubeException extends Exception {
    public JTubeException() {

    }

    JTubeException(String message) {
        super(message);
    }

    JTubeException(String message, Throwable cause) {
        super(message, cause);
    }

    JTubeException(Throwable cause) {
        super(cause);
    }

    JTubeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

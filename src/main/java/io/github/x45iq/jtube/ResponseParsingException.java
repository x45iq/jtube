package io.github.x45iq.jtube;

public class ResponseParsingException extends RuntimeException {
    ResponseParsingException() {
    }

    ResponseParsingException(String message) {
        super(message);
    }

    ResponseParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    ResponseParsingException(Throwable cause) {
        super(cause);
    }

    ResponseParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

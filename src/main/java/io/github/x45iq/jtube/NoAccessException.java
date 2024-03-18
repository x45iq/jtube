package io.github.x45iq.jtube;
/**
 * @author Artem Shein
 */
public class NoAccessException extends RuntimeException {
    NoAccessException() {
    }

    NoAccessException(String message) {
        super(message);
    }

    NoAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    NoAccessException(Throwable cause) {
        super(cause);
    }

    NoAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

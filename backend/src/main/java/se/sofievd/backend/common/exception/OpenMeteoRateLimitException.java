package se.sofievd.backend.common.exception;

public class OpenMeteoRateLimitException extends RuntimeException {
    public OpenMeteoRateLimitException(String message) {
        super(message);
    }
}

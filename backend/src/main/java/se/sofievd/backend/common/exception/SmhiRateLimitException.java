package se.sofievd.backend.common.exception;

public class SmhiRateLimitException extends RuntimeException {
    public SmhiRateLimitException(String message) {
        super(message);
    }
}

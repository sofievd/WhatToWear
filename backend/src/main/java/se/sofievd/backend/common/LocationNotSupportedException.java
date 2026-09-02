package se.sofievd.backend.common;

public class LocationNotSupportedException extends RuntimeException {

    public LocationNotSupportedException(String message) {
        super(message);
    }
}

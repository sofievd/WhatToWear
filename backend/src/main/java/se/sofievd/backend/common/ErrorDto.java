package se.sofievd.backend.common;

import org.springframework.http.HttpStatus;

public record ErrorDto(HttpStatus status,
                       String message) {
}

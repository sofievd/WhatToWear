package se.sofievd.backend.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.sofievd.backend.common.exception.LocationNotSupportedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LocationNotSupportedException.class)
    public ResponseEntity<ErrorDto> handleLocationNotSupported(
            LocationNotSupportedException exception
    ){
        ErrorDto errorResponse = new ErrorDto(
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
}

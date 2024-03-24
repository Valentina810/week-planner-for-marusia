package com.github.valentina810.weekplannerformarusia.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInternalException(RuntimeException e) {
        String message = e.getClass().getName() + " " + e.getMessage();
        log.error(message);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(message)
                .path("/webhook")
                .build();
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}
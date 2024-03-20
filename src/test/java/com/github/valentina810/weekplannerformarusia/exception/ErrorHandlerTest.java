package com.github.valentina810.weekplannerformarusia.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

public class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        openMocks(this);
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleInternalException_shouldReturnInternalServerError() {
        RuntimeException runtimeException = new RuntimeException("Сообщение");
        ResponseEntity<ErrorResponse> responseEntity = errorHandler.handleInternalException(runtimeException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleInternalException_shouldReturnErrorResponseWithCorrectFields() {
        RuntimeException runtimeException = new RuntimeException("Сообщение");

        ResponseEntity<ErrorResponse> responseEntity = errorHandler.handleInternalException(runtimeException);
        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatus());
        assertEquals("/webhook", errorResponse.getPath());
        assertEquals(runtimeException.getClass().getName() + " " + runtimeException.getMessage(), errorResponse.getError());
    }
}

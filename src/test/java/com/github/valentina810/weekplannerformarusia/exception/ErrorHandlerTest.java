package com.github.valentina810.weekplannerformarusia.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void getErrorResponse_whenOccurredRuntimeException_thenReturnErrorResponse() {
        RuntimeException runtimeException = new RuntimeException("Сообщение");

        ResponseEntity<ErrorResponse> responseEntity = errorHandler.handleInternalException(runtimeException);
        ErrorResponse errorResponse = responseEntity.getBody();
        assertAll(
                () -> assertEquals(INTERNAL_SERVER_ERROR, errorResponse.getStatus()),
                () -> assertEquals("/webhook", errorResponse.getPath()),
                () -> assertNotNull(errorResponse),
                () -> assertEquals(runtimeException.getClass().getName() + " " + runtimeException.getMessage()
                        , errorResponse.getError())
        );
    }
}

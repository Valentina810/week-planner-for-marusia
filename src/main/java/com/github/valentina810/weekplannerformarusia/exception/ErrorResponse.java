package com.github.valentina810.weekplannerformarusia.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Builder
@Getter
public class ErrorResponse {
    private Date timestamp;
    private HttpStatus status;
    private String error;
    private String path;
}
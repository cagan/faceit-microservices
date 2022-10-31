package com.faceit.userservice.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
public class ErrorResponse {
    @JsonProperty("status_code")
    private HttpStatus statusCode;
    private String message;
    private List<String> errors;

    public ErrorResponse(HttpStatus statusCode, String message, List<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(HttpStatus status, String message, String error) {
        super();
        this.statusCode = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
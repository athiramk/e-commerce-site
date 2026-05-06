package com.athiramk.ecommercesite.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
    private Map<String, String> validationErrors;  // only for validation failures

    // Private constructor — use static factory methods
    private ErrorResponse(int status, String error, String message, String path) {
        this.status    = status;
        this.error     = error;
        this.message   = message;
        this.path      = path;
        this.timestamp = Instant.now();
    }

    // Factory methods
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, path);
    }

    public static ErrorResponse withValidationErrors(int status, String error,
                                                     String message, String path,
                                                     Map<String, String> validationErrors) {
        ErrorResponse response = new ErrorResponse(status, error, message, path);
        response.validationErrors = validationErrors;
        return response;
    }

    // Getters
    public int getStatus()                          { return status; }
    public String getError()                        { return error; }
    public String getMessage()                      { return message; }
    public String getPath()                         { return path; }
    public Instant getTimestamp()                   { return timestamp; }
    public Map<String, String> getValidationErrors(){ return validationErrors; }
}
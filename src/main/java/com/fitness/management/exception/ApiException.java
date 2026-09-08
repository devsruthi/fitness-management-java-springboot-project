package com.fitness.management.exception;

import org.springframework.http.HttpStatus;

/**
 * Shared base for API errors so the global handler can treat
 * ResourceNotFoundException and BusinessRuleException polymorphically.
 */
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;

    protected ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

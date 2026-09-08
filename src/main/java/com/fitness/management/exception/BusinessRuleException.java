package com.fitness.management.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends ApiException {

    public BusinessRuleException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public BusinessRuleException(String message, HttpStatus status) {
        super(message, status);
    }
}

package com.example.tenant_service.exception;

import java.util.Map;

public class CustomValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public CustomValidationException(Map<String, String> errors) {
        super("Validation failed with errors: " + errors.toString());
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}


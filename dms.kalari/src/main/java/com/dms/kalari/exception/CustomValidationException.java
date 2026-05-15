package com.dms.kalari.exception;

import java.util.Map;

public class CustomValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public CustomValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public CustomValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}


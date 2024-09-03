package com.example.common.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private Object resourceId;

    // Existing constructor
    public ResourceNotFoundException(String message, String resourceName, Object resourceId) {
        super(message);
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    // New constructor for convenience
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super("Resource not found");
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    // Getters and setters
    public String getResourceName() {
        return resourceName;
    }

    public Object getResourceId() {
        return resourceId;
    }
}

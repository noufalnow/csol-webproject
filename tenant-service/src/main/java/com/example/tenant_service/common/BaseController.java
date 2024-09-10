package com.example.tenant_service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class BaseController<DTO, S extends BaseService<DTO>> {

    protected final S service;
    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected BaseController(S service) {
        this.service = service;
    }

    // Common GET request to retrieve all resources
    @GetMapping
    public ResponseEntity<List<DTO>> getAll() {
        logger.debug("Received GET request for all resources");
        return ResponseEntity.ok(service.findAll());
    }

    // Common GET request to retrieve a resource by ID
    @GetMapping("/{id}")
    public ResponseEntity<DTO> getById(@PathVariable Long id) {
        logger.debug("Received GET request for resource with id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    // Common POST request to create a resource
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody DTO dto, BindingResult result) {
        return handleRequest(result, () -> service.save(dto), "Resource created successfully",null);
    }

    // Common PUT request to update a resource
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody DTO dto, BindingResult result) {
        return handleRequest(result, () -> service.update(id, dto), "Resource updated successfully",null);
    }

    // Common DELETE request to soft delete a resource
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.debug("Received DELETE request for resource with id: {}", id);
        service.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method for validation and handling the service layer
    protected ResponseEntity<Map<String, Object>> handleRequest(BindingResult result, Supplier<DTO> serviceAction, String successMessage, Map<String, Object> additionalData) {
        Map<String, String> validationErrors = validate(result);  // Validate the input data
        if (!validationErrors.isEmpty()) {
            return buildErrorResponse(validationErrors, "Validation failed");  // Return validation errors
        }
        
        try {
            serviceAction.get();  // Perform the service action (e.g., save, update)

            // Pass the success message and any additional data (like reload_link) to buildResponse
            return buildResponse(successMessage, additionalData);
        } catch (Exception e) {
            return buildErrorResponse(new HashMap<>(), "Error occurred: " + e.getMessage());  // Return error if an exception occurs
        }
    }

    // Helper method to validate the DTO object
    protected Map<String, String> validate(BindingResult result) {
        Map<String, String> validationErrors = new HashMap<>();
        result.getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else if (error instanceof ObjectError) {
                validationErrors.put("global", error.getDefaultMessage());
            }
        });
        return validationErrors;
    }

    // Helper methods to build responses
    protected ResponseEntity<Map<String, Object>> buildResponse(String message, Map<String, Object> additionalData) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        if (additionalData != null && !additionalData.isEmpty()) {
            response.putAll(additionalData);
        }
        return ResponseEntity.ok(response);
    }

    protected ResponseEntity<Map<String, Object>> buildErrorResponse(Map<String, String> errors, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        response.put("errors", errors);
        
        return ResponseEntity.badRequest().body(response);
    }
}

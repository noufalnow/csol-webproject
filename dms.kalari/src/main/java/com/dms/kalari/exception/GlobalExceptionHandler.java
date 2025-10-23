package com.dms.kalari.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dms.kalari.exception.service.EmailService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired(required = false)
    private EmailService emailService;

    @Value("${app.email.notification.enabled:false}")
    private boolean emailNotificationEnabled;

    @Value("${app.email.notification.recipients:}")
    private String[] notificationRecipients;

    @Value("${app.environment:production}")
    private String environment;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        
        // Only send email for 404 errors if configured
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Resource Not Found - 404 Error", 
                buildEmailContent("ResourceNotFoundException", ex.getMessage(), HttpStatus.NOT_FOUND));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            "Resource Not Found",
            "ERR404",
            ex.getResourceName(),
            ex.getResourceId()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Validation failed: {}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Method argument validation failed: {}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage() + " (rejected value: " + error.getRejectedValue() + ")");
        }
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String rootCauseMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "Unknown error";
        log.error("Data integrity violation: {}", rootCauseMessage, ex);

        Map<String, String> errors = mapDbExceptionToFieldErrors(rootCauseMessage);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Data integrity violation");
        response.put("errors", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        
        // Send email for unexpected errors in production
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Unexpected Server Error - 500 Error", 
                buildEmailContent("GeneralException", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            "Unexpected Error",
            "ERR500",
            ex.getMessage(),
            null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private Map<String, String> mapDbExceptionToFieldErrors(String dbMessage) {
        Map<String, String> errors = new HashMap<>();

        Pattern pattern = Pattern.compile("\\(([a-z_]+)::text\\)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(dbMessage);
        
        if (matcher.find()) {
            String columnName = matcher.group(1).trim();
            String dtoField = toCamelCase(columnName);
            errors.put(dtoField, "Value already exists");
        } else {
            Pattern fallbackPattern = Pattern.compile("Key \\(.*?([a-z_]+).*?\\)=\\(.+\\) already exists", Pattern.CASE_INSENSITIVE);
            Matcher fallbackMatcher = fallbackPattern.matcher(dbMessage);
            if (fallbackMatcher.find()) {
                String columnName = fallbackMatcher.group(1).trim();
                if (!columnName.equals("lower")) {
                    String dtoField = toCamelCase(columnName);
                    errors.put(dtoField, "Value already exists");
                } else {
                    errors.put("error", "Duplicate value exists");
                }
            } else {
                errors.put("error", "Duplicate value exists");
            }
        }

        return errors;
    }

    private static String toCamelCase(String s) {
        String[] parts = s.split("_");
        StringBuilder sb = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i].substring(0, 1).toUpperCase())
              .append(parts[i].substring(1).toLowerCase());
        }
        return sb.toString();
    }

    private boolean shouldSendEmailNotification() {
        return emailNotificationEnabled && 
               emailService != null && 
               notificationRecipients != null && 
               notificationRecipients.length > 0 &&
               "production".equalsIgnoreCase(environment);
    }

    private void sendErrorEmail(String subject, String content) {
        try {
            for (String recipient : notificationRecipients) {
                emailService.sendMail(recipient, subject, content);
            }
            log.debug("Error notification email sent successfully");
        } catch (Exception emailEx) {
            log.error("Failed to send error notification email: {}", emailEx.getMessage(), emailEx);
            // Don't throw exception to avoid masking the original error
        }
    }

 // In your GlobalExceptionHandler, update the email content method:

    private String buildEmailContent(String exceptionType, String errorMessage, HttpStatus status) {
        return String.format(
            "🚨 ERROR ALERT - Indian Kalari Payattu Federation Application\n\n" +
            "Environment: %s\n" +
            "Timestamp: %s\n" +
            "Error Type: %s\n" +
            "HTTP Status: %d %s\n" +
            "Error Details: %s\n\n" +
            "Please check the application logs for more details.\n\n" +
            "---\n" +
            "This is an automated notification from Kalari Application Error Handler",
            environment.toUpperCase(),
            LocalDateTime.now().format(formatter),
            exceptionType,
            status.value(),
            status.getReasonPhrase(),
            errorMessage
        );
    }

    // ErrorResponse class remains unchanged
    public static class ErrorResponse {
        private int status;
        private String message;
        private String errorType;
        private String errorCode;
        private String resource;
        private Object resourceId;
        private LocalDateTime timestamp;

        public ErrorResponse(int status, String message, String errorType, String errorCode, String resource, Object resourceId) {
            this.status = status;
            this.message = message + " - Error occurred";
            this.errorType = errorType;
            this.errorCode = errorCode;
            this.resource = resource;
            this.resourceId = resourceId;
            this.timestamp = LocalDateTime.now();
        }

        // Getters and setters
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getErrorType() { return errorType; }
        public void setErrorType(String errorType) { this.errorType = errorType; }
        public String getErrorCode() { return errorCode; }
        public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
        public String getResource() { return resource; }
        public void setResource(String resource) { this.resource = resource; }
        public Object getResourceId() { return resourceId; }
        public void setResourceId(Object resourceId) { this.resourceId = resourceId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }

    // ValidationErrorResponse class remains unchanged
    public static class ValidationErrorResponse extends ErrorResponse {
        private Map<String, String> validationErrors;

        public ValidationErrorResponse(int status, String message, Map<String, String> validationErrors) {
            super(status, message, "Validation Error", "ERR400", null, null);
            this.validationErrors = validationErrors;
        }

        public Map<String, String> getValidationErrors() { return validationErrors; }
        public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }
    }
}
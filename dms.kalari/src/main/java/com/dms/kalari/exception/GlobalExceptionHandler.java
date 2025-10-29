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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.transaction.TransactionSystemException;


import com.dms.kalari.exception.service.EmailService;
import com.dms.kalari.security.CustomUserPrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import com.google.zxing.WriterException;

import java.io.IOException;
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
    
    // Generic messages for production users
    private static final String GENERIC_ERROR_MESSAGE = "Sorry, something went wrong. Please try again later.";
    private static final String GENERIC_NOT_FOUND_MESSAGE = "The requested resource was not found.";
    private static final String GENERIC_VALIDATION_MESSAGE = "Validation failed for the request.";
    private static final String GENERIC_ACCESS_DENIED_MESSAGE = "Access denied.";
    private static final String GENERIC_BAD_REQUEST_MESSAGE = "Invalid request.";

    // ===================== CUSTOM EXCEPTIONS ========================= //

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Resource Not Found - 404 Error", 
                buildDetailedEmailContent("ResourceNotFoundException", ex, HttpStatus.NOT_FOUND));
        }

        String userMessage = isProduction() ? GENERIC_NOT_FOUND_MESSAGE : ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            userMessage,
            "Resource Not Found",
            "ERR404",
            isProduction() ? null : ex.getResourceName(),
            isProduction() ? null : ex.getResourceId()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleCustomValidationException(CustomValidationException ex) {
        log.error("Custom validation failed: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Custom Validation Failed - 400 Error", 
                buildDetailedEmailContent("CustomValidationException", ex, HttpStatus.BAD_REQUEST));
        }

        String userMessage = isProduction() ? GENERIC_VALIDATION_MESSAGE : ex.getMessage();

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            userMessage,
            ex.getErrors() != null ? ex.getErrors() : new HashMap<>()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ===================== SECURITY & AUTHENTICATION EXCEPTIONS ========================= //

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException ex) {
        log.warn("Security violation: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Security Violation - 403 Error", 
                buildDetailedEmailContent("SecurityException", ex, HttpStatus.FORBIDDEN));
        }

        String userMessage = isProduction() ? GENERIC_ACCESS_DENIED_MESSAGE : ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            userMessage,
            "Security Violation",
            "ERR403",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Access Denied - 403 Error", 
                buildDetailedEmailContent("AccessDeniedException", ex, HttpStatus.FORBIDDEN));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            GENERIC_ACCESS_DENIED_MESSAGE,
            "Access Denied",
            "ERR403",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException ex) {
        log.warn("Account disabled: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Your account is disabled. Please contact administrator.",
            "Account Disabled",
            "ERR403",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    // ===================== VALIDATION EXCEPTIONS ========================= //

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Validation failed: {}", ex.getMessage(), ex);
        
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Constraint Violation - 400 Error", 
                buildDetailedEmailContent("ConstraintViolationException", ex, HttpStatus.BAD_REQUEST));
        }

        String userMessage = isProduction() ? GENERIC_VALIDATION_MESSAGE : "Validation failed: " + ex.getMessage();
        
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            userMessage,
            errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Method argument validation failed: {}", ex.getMessage(), ex);
        
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String errorMessage = isProduction() ? 
                error.getDefaultMessage() : 
                error.getDefaultMessage() + " (rejected value: " + error.getRejectedValue() + ")";
            errors.put(error.getField(), errorMessage);
        }
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Method Argument Validation Failed - 400 Error", 
                buildDetailedEmailContent("MethodArgumentNotValidException", ex, HttpStatus.BAD_REQUEST));
        }

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            GENERIC_VALIDATION_MESSAGE,
            errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ===================== DATABASE & PERSISTENCE EXCEPTIONS ========================= //

    @ExceptionHandler({ DataIntegrityViolationException.class, TransactionSystemException.class })
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(Exception ex) {
        Throwable root = getRootCause(ex);
        String rootMessage = root != null ? root.getMessage() : "Unknown data integrity error";
        log.error("Data integrity violation: {}", rootMessage, ex); // Log only, no email

        Map<String, String> errors = mapDbExceptionToFieldErrors(rootMessage);

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Data integrity violation: " + rootMessage);
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private Throwable getRootCause(Throwable ex) {
        Throwable cause = ex.getCause();
        while (cause != null && cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("Entity not found: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Entity Not Found - 404 Error", 
                buildDetailedEmailContent("EntityNotFoundException", ex, HttpStatus.NOT_FOUND));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            GENERIC_NOT_FOUND_MESSAGE,
            "Entity Not Found",
            "ERR404",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // ===================== RUNTIME & ILLEGAL STATE EXCEPTIONS ========================= //

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage(), ex);
        
        String userMessage = isProduction() ? GENERIC_BAD_REQUEST_MESSAGE : ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            userMessage,
            "Invalid Argument",
            "ERR400",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        log.error("Illegal state: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Illegal State - 500 Error", 
                buildDetailedEmailContent("IllegalStateException", ex, HttpStatus.INTERNAL_SERVER_ERROR));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            GENERIC_ERROR_MESSAGE,
            "Illegal State",
            "ERR500",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("Runtime Exception - 500 Error", 
                buildDetailedEmailContent("RuntimeException", ex, HttpStatus.INTERNAL_SERVER_ERROR));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            GENERIC_ERROR_MESSAGE,
            "Runtime Error",
            "ERR500",
            isProduction() ? null : ex.getClass().getSimpleName(),
            isProduction() ? null : ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // ===================== IO & JSON PROCESSING EXCEPTIONS ========================= //

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        log.error("IO operation failed: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("IO Exception - 500 Error", 
                buildDetailedEmailContent("IOException", ex, HttpStatus.INTERNAL_SERVER_ERROR));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "File operation failed. Please try again.",
            "IO Error",
            "ERR500",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("JSON processing failed: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid JSON data provided.",
            "JSON Processing Error",
            "ERR400",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Malformed JSON request: {}", ex.getMessage(), ex);
        
        String userMessage = isProduction() ? GENERIC_BAD_REQUEST_MESSAGE : "Malformed JSON request";

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            userMessage,
            "Malformed Request",
            "ERR400",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ===================== QR CODE & WRITER EXCEPTIONS ========================= //

    @ExceptionHandler(WriterException.class)
    public ResponseEntity<ErrorResponse> handleWriterException(WriterException ex) {
        log.error("QR code generation failed: {}", ex.getMessage(), ex);
        
        if (shouldSendEmailNotification()) {
            sendErrorEmail("QR Code Generation Failed - 500 Error", 
                buildDetailedEmailContent("WriterException", ex, HttpStatus.INTERNAL_SERVER_ERROR));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "QR code generation failed. Please try again.",
            "QR Generation Error",
            "ERR500",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // ===================== HTTP & REQUEST EXCEPTIONS ========================= //

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        log.error("Missing request parameter: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Missing required parameter: " + ex.getParameterName(),
            "Missing Parameter",
            "ERR400",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        log.error("Method argument type mismatch: {}", ex.getMessage(), ex);
        
        String userMessage = isProduction() ? 
            GENERIC_BAD_REQUEST_MESSAGE : 
            "Invalid parameter type for: " + ex.getName();

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            userMessage,
            "Type Mismatch",
            "ERR400",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.error("No handler found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            GENERIC_NOT_FOUND_MESSAGE,
            "Endpoint Not Found",
            "ERR404",
            null,
            null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // ===================== GENERIC EXCEPTION HANDLER ========================= //

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Detect Spring 404-style exceptions
        if (ex instanceof org.springframework.web.servlet.resource.NoResourceFoundException
                || ex instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            status = HttpStatus.NOT_FOUND;
        }

        log.error("Error [{} {}]: {}", status.value(), status.getReasonPhrase(), ex.getMessage());

        // Skip email for 404-like exceptions (bot scans, static resources, etc.)
        if (status == HttpStatus.NOT_FOUND) {
            log.warn("Skipping email for 404 resource: {}", request.getRequestURI());
        } else if (shouldSendEmailNotification()) {
            sendErrorEmail(
                "Unexpected Server Error - " + status.value() + " " + status.getReasonPhrase(),
                buildDetailedEmailContent("GenericException", ex, status)
            );
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", isProduction()
            ? "Error processing request: " + GENERIC_ERROR_MESSAGE
            : "Error processing request: " + ex.getMessage());

        return ResponseEntity.status(status).body(response);
    }


    // ===================== HELPER METHODS ========================= //

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

    private boolean isProduction() {
        return "production".equalsIgnoreCase(environment);
    }

    private boolean shouldSendEmailNotification() {
        return emailNotificationEnabled && 
               emailService != null && 
               notificationRecipients != null && 
               notificationRecipients.length > 0 &&
               isProduction();
    }

    private void sendErrorEmail(String subject, String content) {
        if (!shouldSendEmailNotification()) {
            return;
        }
        
        try {
            CustomUserPrincipal principal = getCurrentUserPrincipal();
            
            for (String recipient : notificationRecipients) {
                emailService.sendMail(recipient, subject, content, principal);
            }
            
            String userInfo = principal != null ? 
                " (triggered by user: " + principal.getUsername() + ")" : 
                " (system/background error)";
            log.debug("Error notification emails sent successfully{}", userInfo);
            
        } catch (Exception emailEx) {
            log.error("Failed to send error notification email: {}", emailEx.getMessage(), emailEx);
        }
    }
    
    private CustomUserPrincipal getCurrentUserPrincipal() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            // Check if we have a valid authenticated user
            if (authentication != null && 
                authentication.isAuthenticated() && 
                authentication.getPrincipal() instanceof CustomUserPrincipal) {
                
                return (CustomUserPrincipal) authentication.getPrincipal();
            }
        } catch (Exception e) {
            log.debug("No authenticated user found in security context: {}", e.getMessage());
        }
        return null; // Return null for unauthenticated/system errors
    }

    private String buildDetailedEmailContent(String exceptionType, Exception ex, HttpStatus status) {
        StringBuilder content = new StringBuilder();
        
        content.append(String.format(
            "🚨 ERROR ALERT - Indian Kalari Payattu Federation Application\n\n" +
            "Environment: %s\n" +
            "Timestamp: %s\n" +
            "Error Type: %s\n" +
            "HTTP Status: %d %s\n" +
            "Exception Class: %s\n" +
            "Error Message: %s\n\n",
            environment.toUpperCase(),
            LocalDateTime.now().format(formatter),
            exceptionType,
            status.value(),
            status.getReasonPhrase(),
            ex.getClass().getName(),
            ex.getMessage()
        ));
        
        if (ex.getCause() != null) {
            content.append(String.format(
                "Root Cause: %s\n" +
                "Root Cause Message: %s\n\n",
                ex.getCause().getClass().getName(),
                ex.getCause().getMessage()
            ));
        }
        
        content.append("Complete Stack Trace:\n");
        for (StackTraceElement element : ex.getStackTrace()) {
            content.append("    at ").append(element.toString()).append("\n");
        }
        
        if (ex.getSuppressed() != null && ex.getSuppressed().length > 0) {
            content.append("\nSuppressed Exceptions:\n");
            for (Throwable suppressed : ex.getSuppressed()) {
                content.append("    ").append(suppressed.getClass().getName())
                       .append(": ").append(suppressed.getMessage()).append("\n");
            }
        }
        
        content.append("\n---\n");
        content.append("This is an automated notification from Kalari Application Error Handler");
        
        return content.toString();
    }

    // ===================== RESPONSE CLASSES ========================= //

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
            this.message = message;
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
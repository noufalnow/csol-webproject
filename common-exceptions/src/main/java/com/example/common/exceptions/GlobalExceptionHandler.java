package com.example.tenant_service.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.NOT_FOUND.value(),
			ex.getMessage(),
			"Resource Not Found",
			ex.getResourceName(),
			ex.getResourceId()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
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
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		ValidationErrorResponse errorResponse = new ValidationErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			"Validation failed",
			errors
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.CONFLICT.value(),
			"Data integrity violation",
			"Data Integrity Violation",
			ex.getRootCause() != null ? ex.getRootCause().getMessage() : "Unknown error",
			null
		);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			"An unexpected error occurred",
			"Unexpected Error",
			ex.getMessage(),
			null
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	public static class ErrorResponse {
		private int status;
		private String message;
		private String errorType;
		private String resource;
		private Object resourceId;

		public ErrorResponse(int status, String message, String errorType, String resource, Object resourceId) {
			this.status = status;
			this.message = message + " G-Msg";
			this.errorType = errorType;
			this.resource = resource;
			this.resourceId = resourceId;
		}

		// Getters and setters
		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getErrorType() {
			return errorType;
		}

		public void setErrorType(String errorType) {
			this.errorType = errorType;
		}

		public String getResource() {
			return resource;
		}

		public void setResource(String resource) {
			this.resource = resource;
		}

		public Object getResourceId() {
			return resourceId;
		}

		public void setResourceId(Object resourceId) {
			this.resourceId = resourceId;
		}
	}

	public static class ValidationErrorResponse extends ErrorResponse {
		private Map<String, String> validationErrors;

		public ValidationErrorResponse(int status, String message, Map<String, String> validationErrors) {
			super(status, message, "Validation Error", null, null);
			this.validationErrors = validationErrors;
		}

		// Getters and setters
		public Map<String, String> getValidationErrors() {
			return validationErrors;
		}

		public void setValidationErrors(Map<String, String> validationErrors) {
			this.validationErrors = validationErrors;
		}
	}
}

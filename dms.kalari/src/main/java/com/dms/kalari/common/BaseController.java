package com.dms.kalari.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.exception.GlobalExceptionHandler;

import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @GetMapping
    public ResponseEntity<List<DTO>> getAll() {
        logger.debug("Received GET request for all resources");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTO> getById(@PathVariable Long id) {
        logger.debug("Received GET request for resource with id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody DTO dto, BindingResult result) {
        return handleRequest(result, () -> service.save(dto), "Resource created successfully", null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody DTO dto, BindingResult result) {
        return handleRequest(result, () -> service.update(id, dto), "Resource updated successfully", null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.debug("Received DELETE request for resource with id: {}", id);
        service.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<Map<String, Object>> handleRequest(
            BindingResult result,
            Supplier<DTO> serviceAction,
            String successMessage,
            Map<String, Object> additionalData) {

        Map<String, String> validationErrors = validate(result);
        if (!validationErrors.isEmpty()) {
            return buildErrorResponse(validationErrors, "Validation failed");
        }

        try {
            DTO resultDto = serviceAction.get();
            return buildResponse(successMessage, additionalData);
        } catch (Exception e) {
            // ✅ Let Spring handle DB constraint violations
            if (e.getCause() instanceof org.springframework.dao.DataIntegrityViolationException) {
                throw (org.springframework.dao.DataIntegrityViolationException) e.getCause();
            }
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                throw (org.springframework.dao.DataIntegrityViolationException) e;
            }

            // For other exceptions, keep your current fallback
            return buildErrorResponse(new HashMap<>(), "Error occurred: " + e.getMessage());
        }
    }



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

    protected void setupPagination(Model model, Page<DTO> page, String sortField, String sortDir) {
        /*if (page == null || page.isEmpty()) {
            // Handle the case when page is null or empty (e.g., no results)
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalItems", 0);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
            model.addAttribute("startPage", 0);
            model.addAttribute("endPage", 0);
            return;
        }*/

        int totalPages = page.getTotalPages();
        int currentPage = page.getNumber();

        model.addAttribute("items", page.getContent());  // Consistent naming
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        Map<String, String> sortStatus = new HashMap<>();
        sortStatus.put(sortField, sortDir);
        model.addAttribute("sortStatus", sortStatus);

        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }

    
    
    protected void logInfo(String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    protected void logError(String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.error(message, throwable);
        }
    }
}

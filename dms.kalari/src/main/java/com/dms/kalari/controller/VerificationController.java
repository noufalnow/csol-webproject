package com.dms.kalari.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.events.service.EventService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/verify")
public class VerificationController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationController.class);
    private final EventService eventService;

    public VerificationController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String verifyCertificate(@RequestParam String id, Model model) {
        try {
            //logger.debug("Starting certificate verification for ID: {}", id);
            
            String[] parts = validateAndParseId(id);
            Long memvntId = Long.parseLong(parts[0]);
            Long itemId = parseOptionalItemId(parts);

            List<Object[]> result = fetchVerificationResults(itemId, memvntId);
            //logResults(result);
            
            model.addAttribute("results", result);
            model.addAttribute("success", true);
            
            //logger.info("Successfully verified certificate for ID: {}", id);
            return "fragments/admin/users/profile/verification-result";
            
        } catch (NumberFormatException e) {
            return handleError(model, "Invalid ID format: must be in 'memvntId-itemId' format with numbers", e);
        } catch (IllegalArgumentException e) {
            return handleError(model, e.getMessage(), e);
        } catch (Exception e) {
            return handleError(model, "Certificate verification failed", e);
        }
    }

    private String[] validateAndParseId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Verification ID cannot be empty");
        }
        String[] parts = id.split("-");
        if (parts.length == 0) {
            throw new IllegalArgumentException("Invalid ID format");
        }
        return parts;
    }

    private Long parseOptionalItemId(String[] parts) {
        return parts.length > 1 ? Long.parseLong(parts[1]) : null;
    }

    private List<Object[]> fetchVerificationResults(Long itemId, Long memvntId) {
        return eventService.getMemberEventsWithFilters(
            itemId, null, null, null, null, true, memvntId
        );
    }

    private void logResults(List<Object[]> results) {
        if (logger.isDebugEnabled()) {
            logger.debug("Verification results:");
            for (int i = 0; i < results.size(); i++) {
                Object[] row = results.get(i);
                logger.debug("Row {}: {}", i, row != null ? Arrays.toString(row) : "null");
            }
        }
    }

    private String handleError(Model model, String message, Exception e) {
        logger.error(message, e);
        model.addAttribute("error", message);
        return "verification-error";
    }
}
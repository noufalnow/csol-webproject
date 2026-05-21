package com.dms.kalari.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.events.service.EventService;
import com.dms.kalari.util.SimpleBase64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/verify")
public class VerificationController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationController.class);
    
    private Long decodeVerificationId(
	        String encodedId) {

	    try {

	        String decoded =
	                SimpleBase64.decode(
	                        encodedId
	                );

	        String[] parts =
	                decoded.split(
	                        "\\|",
	                        2
	                );

	        return Long.parseLong(
	                parts[0]
	        );

	    } catch (Exception ex) {

	        throw new IllegalArgumentException(
	                "Invalid verification code",
	                ex
	        );
	    }
	}

	public record DecodedCertificate(
	        Long meiId,
	        Long itemId
	) {}
    private final EventService eventService;

    public VerificationController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String verifyCertificate(
            @RequestParam String id,
            Model model) {

        try {

            Long meiId = decodeVerificationId(id);

            List<Object[]> result =
                    eventService.getMemberEventVerification(meiId);

            if (result.isEmpty()) {
                throw new IllegalArgumentException(
                    "Certificate not found"
                );
            }

            model.addAttribute("results", result);
            model.addAttribute("success", true);

            return "fragments/manage/members/profile/verification-result";
            

        } catch (Exception e) {

            return handleError(
                model,
                "Invalid verification code",
                e
            );
        }
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
package com.dms.kalari.exception.service;

import com.dms.kalari.security.CustomUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send email to single recipient with user principal context
     */
    @Async
    public void sendMail(String to, String subject, String body, CustomUserPrincipal principal) {
        try {
            String traceInfo = buildUserTraceInfo(principal);
            SimpleMailMessage message = createEmailMessage(to, subject, body + traceInfo);
            mailSender.send(message);

            logEmailSuccess(to, principal);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }

    /**
     * Send email to multiple recipients with automatic user context detection
     */
    @Async
    public void sendMailToMultiple(String[] recipients, String subject, String body) {
        try {
            CustomUserPrincipal principal = getCurrentUserPrincipal();
            String traceInfo = buildUserTraceInfo(principal);
            SimpleMailMessage message = createEmailMessage(recipients, subject, body + traceInfo);
            mailSender.send(message);

            logMultipleEmailSuccess(principal);
        } catch (Exception e) {
            log.error("Failed to send email to multiple recipients: {}", e.getMessage(), e);
        }
    }

    /**
     * Create email message for single recipient
     */
    private SimpleMailMessage createEmailMessage(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    /**
     * Create email message for multiple recipients
     */
    private SimpleMailMessage createEmailMessage(String[] recipients, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipients);
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    /**
     * Safely get the current authenticated user principal
     */
    private CustomUserPrincipal getCurrentUserPrincipal() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (isValidAuthentication(authentication)) {
                return (CustomUserPrincipal) authentication.getPrincipal();
            }
        } catch (Exception e) {
            log.debug("Could not retrieve user principal: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Validate authentication object
     */
    private boolean isValidAuthentication(Authentication authentication) {
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal()) &&
               authentication.getPrincipal() instanceof CustomUserPrincipal;
    }

    /**
     * Build user trace information for email footer
     */
    private String buildUserTraceInfo(CustomUserPrincipal principal) {
        if (principal == null) {
            return String.format(
                "\n\n---\nContext: System/Unauthenticated Request\n" +
                "Timestamp: %s\n" +
                "Source: Application Error Handler\n",
                LocalDateTime.now().format(TIMESTAMP_FORMATTER)
            );
        }
        
        return String.format(
            "\n\n---\nUSER CONTEXT:\n" +
            "• Name: %s\n" +
            "• Email: %s\n" +
            "• User ID: %d\n" +
            "• Node: %s (%s)\n" +
            "• Type: %s\n" +
            "• Role: %d | Institution: %d\n" +
            "• Timestamp: %s\n",
            principal.getUserFullName(),
            principal.getUsername(),
            principal.getUserId(),
            principal.getNodeName(),
            safeEnumToString(principal.getNodeType()),
            safeEnumToString(principal.getUserType()),
            principal.getRoleId(),
            principal.getInstId(),
            LocalDateTime.now().format(TIMESTAMP_FORMATTER)
        );
    }

    /**
     * Safely convert enum to string
     */
    private String safeEnumToString(Enum<?> enumValue) {
        return enumValue != null ? enumValue.name() : "N/A";
    }

    /**
     * Log successful email sending for single recipient
     */
    private void logEmailSuccess(String to, CustomUserPrincipal principal) {
        String userContext = principal != null ? 
            "by user: " + principal.getUsername() : "by system";
        log.debug("Email sent successfully to {} {}", to, userContext);
    }

    /**
     * Log successful email sending for multiple recipients
     */
    private void logMultipleEmailSuccess(CustomUserPrincipal principal) {
        String userContext = principal != null ? 
            "by user: " + principal.getUsername() : "by system";
        log.debug("Email sent successfully to multiple recipients {}", userContext);
    }
    
    public void sendEmail(String[] recipients, String subject, String body) {
        SimpleMailMessage message = createEmailMessage(recipients, subject, body);
        mailSender.send(message);
    }
}
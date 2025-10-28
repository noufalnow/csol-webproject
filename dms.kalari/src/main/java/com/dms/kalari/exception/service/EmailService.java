package com.dms.kalari.exception.service;

import com.dms.kalari.security.CustomUserPrincipal;

import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final TemplateEngine templateEngine;
    private static final String DEFAULT_FROM = "admin@indiankalaripayattufederation.com";

    public EmailService(JavaMailSender mailSender,TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
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
            message.setFrom("Indian Kalaripayattu Federation <admin@indiankalaripayattufederation.com>");
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
        message.setFrom("Indian Kalaripayattu Federation <admin@indiankalaripayattufederation.com>");
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
        message.setFrom("Indian Kalaripayattu Federation <admin@indiankalaripayattufederation.com>");
        mailSender.send(message);
    }
    
    
    @Async
    public void sendHtmlMail(String[] to, String subject, String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("admin@indiankalaripayattufederation.com", "Indian Kalaripayattu Federation");
            helper.setTo("admin@indiankalaripayattufederation.com");
            helper.setBcc("admin@indiankalaripayattufederation.com");
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true => HTML

            mailSender.send(mimeMessage);

            log.debug("✅ HTML email sent successfully to {}", (Object) to);
        } catch (Exception e) {
            log.error("❌ Failed to send HTML email: {}", e.getMessage(), e);
        }
    }
}
package com.dms.kalari.config;

import com.dms.kalari.exception.service.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class GlobalOAuth2ExceptionHandler {

    @Autowired
    private EmailService emailService;

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public void handleOAuth2Exception(OAuth2AuthenticationException ex,
                                      HttpServletResponse response) throws IOException {

        String message = ex.getMessage();

        // default message
        String friendlyMessage = "Your login session expired. Please try signing in again.";

        if (message.contains("authorization_request_not_found")) {
            friendlyMessage = "You denied the access request.";
        }

        if (message.contains("User not found")) {
            friendlyMessage = "No account exists for your Google login. Please contact admin.";

            // 🔔 Send an email alert to admin
            String[] recipients = {"admin@indiankalaripayattufederation.com"};  // change to your actual admin email
            String subject = "OAuth2 Login Attempt - User Not Found";
            String body = "An OAuth2 login attempt failed because the user does not exist.\n\n"
                        + "Details:\n"
                        + "Error: " + message + "\n\n"
                        + "Please review if this user needs to be added manually.";
            emailService.sendEmail(recipients, subject, body);
        }

        String encoded = URLEncoder.encode(friendlyMessage, StandardCharsets.UTF_8);
        response.sendRedirect("/auth-error?error=" + encoded);
    }
}

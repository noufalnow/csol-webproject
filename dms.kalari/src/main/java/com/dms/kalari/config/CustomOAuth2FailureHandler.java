package com.dms.kalari.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        
        // Optional: log error
        System.err.println("OAuth2 login failed: " + exception.getMessage());
        
        String message = exception.getMessage();
        if (message.contains("authorization_request_not_found")) {
            message = "Your login session expired. Please try signing in again.";
        } else if (message.contains("access_denied")) {
            message = "You denied the access request.";
        }

        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        response.sendRedirect("/auth-error?error=" + encodedMessage);

    }
}

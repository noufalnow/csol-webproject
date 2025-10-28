package com.dms.kalari.config;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class GlobalOAuth2ExceptionHandler {

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public void handleOAuth2Exception(OAuth2AuthenticationException ex, HttpServletResponse response,AuthenticationException exception) throws IOException {
        String errorMsg = "Your login session expired. Please try signing in again.";
        response.sendRedirect("/auth-error?error=" + java.net.URLEncoder.encode(errorMsg, java.nio.charset.StandardCharsets.UTF_8));
        
        
        String message = exception.getMessage();
        if (message.contains("authorization_request_not_found")) {
            message = "You denied the access request.";
        }
        
        
        if (message.contains("User not found")) {
            message = "No account exists for your Google login. Please contact admin.";
        }
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        response.sendRedirect("/auth-error?error=" + encoded);

    }
}


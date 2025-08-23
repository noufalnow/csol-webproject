package com.dms.kalari.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class RequestAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final PrivilegeChecker privilegeChecker;

    public RequestAuthorizationManager(PrivilegeChecker privilegeChecker) {
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        
        // Skip security checks for internal forwards
        if (isInternalForward(request)) {
            return new AuthorizationDecision(true); // Allow all internal forwards
        }
        
        String requestUri = getOriginalRequestUri(request);
        boolean hasAccess = privilegeChecker.hasAccess(requestUri);
        
        return new AuthorizationDecision(hasAccess);
    }

    private boolean isInternalForward(HttpServletRequest request) {
        // Check if this is an internal forward
        return request.getAttribute("jakarta.servlet.forward.request_uri") != null;
    }

    private String getOriginalRequestUri(HttpServletRequest request) {
        // Get the original request URI for forwarded requests
        String forwardUri = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
        return forwardUri != null ? forwardUri : request.getRequestURI();
    }
}
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
        
        String requestUri = request.getRequestURI();
        boolean hasAccess = privilegeChecker.hasAccess(requestUri);
        
        return new AuthorizationDecision(hasAccess);
    }

    private boolean isInternalForward(HttpServletRequest request) {
        // Check if this is an internal forward (not an original HTTP request)
        return request.getAttribute("javax.servlet.forward.request_uri") != null ||
               request.getAttribute("jakarta.servlet.forward.request_uri") != null;
    }
}
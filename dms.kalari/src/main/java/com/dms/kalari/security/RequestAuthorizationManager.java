package com.dms.kalari.security;

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
        String requestUri = context.getRequest().getRequestURI();
        String method = context.getRequest().getMethod();
        
        boolean hasAccess = privilegeChecker.hasAccess(requestUri, method);
        return new AuthorizationDecision(hasAccess);
    }
}
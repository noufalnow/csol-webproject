package com.dms.kalari.security;

import com.dms.kalari.admin.service.AuthLogActionService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Centralized Action Logger Aspect
 * Logs POST/PUT/DELETE requests after a successful DB operation
 * using AuthLogActionService.logPostAction()
 */
@Aspect
@Component
public class ActionLoggingAspect {

    private final AuthLogActionService authLogActionService;
    private final HttpServletRequest request;

    public ActionLoggingAspect(AuthLogActionService authLogActionService, HttpServletRequest request) {
        this.authLogActionService = authLogActionService;
        this.request = request;
    }

    @AfterReturning(value = "execution(* com.dms.kalari..controller..*(..))", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, Object result) {
        Long loginId = (Long) request.getSession().getAttribute("loginId");
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // Only log for data-changing operations
        if ("POST".equalsIgnoreCase(method) || 
            "PUT".equalsIgnoreCase(method) || 
            "DELETE".equalsIgnoreCase(method)) {

            authLogActionService.logPostAction(loginId, uri, method, result);
        }
    }
}

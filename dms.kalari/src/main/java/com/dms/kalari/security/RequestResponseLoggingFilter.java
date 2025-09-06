package com.dms.kalari.security;

import com.dms.kalari.admin.entity.AuthLogAction;

import com.dms.kalari.admin.service.AuthLogActionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import com.dms.kalari.util.XorUrlUnMasker;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final AuthLogActionService authLogActionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RequestResponseLoggingFilter(AuthLogActionService authLogActionService) {
        this.authLogActionService = authLogActionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            if (isWritableMethod(request) && wrappedResponse.getStatus() < 400) {
            	
            	
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Long loginId = null;

                if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserPrincipal user) {
                    loginId = user.getLoginId();
                }
            	
            	
                try {
                    // ✅ Extract logged-in user from Spring Security


                    String payload = new String(wrappedRequest.getContentAsByteArray(),
                                                request.getCharacterEncoding());

                    // Parse and sanitize the payload
                    Map<String, Object> parsedPayload = parseAndSanitizePayload(payload);

                    authLogActionService.logPostAction(
                        loginId,
                        XorUrlUnMasker.unmaskUri(request.getRequestURI()),
                        request.getMethod(),
                        parsedPayload
                    );

                } catch (Exception e) {
                    authLogActionService.logPostAction(
                    	loginId,
                    	XorUrlUnMasker.unmaskUri(request.getRequestURI()),
                        request.getMethod(),
                        Map.of("phase", "POST", "error", "payload-capture-failed")
                    );
                }
            }

            wrappedResponse.copyBodyToResponse();
        }
    }

    private boolean isWritableMethod(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
            || "PUT".equalsIgnoreCase(request.getMethod())
            || "DELETE".equalsIgnoreCase(request.getMethod());
    }

    private Map<String, Object> parseAndSanitizePayload(String payload) {
        try {
            if (payload == null || payload.trim().isEmpty()) {
                return Map.of();
            }

            Map<String, Object> parsed = Arrays.stream(payload.split("&"))
            	    .map(kv -> kv.split("=", 2))
            	    .collect(Collectors.toMap(
            	        kv -> kv[0],
            	        kv -> {
            	            if (kv.length > 1) {
            	                try {
            	                    return java.net.URLDecoder.decode(kv[1], "UTF-8");
            	                } catch (Exception e) {
            	                    return kv[1]; // fallback to raw value
            	                }
            	            } else {
            	                return "";
            	            }
            	        },
            	        (a, b) -> b,
            	        LinkedHashMap::new
            	    ));


            // Remove sensitive keys
            parsed.remove("_csrf");
            parsed.remove("password");
            parsed.remove("secretKey");
            parsed.remove("jwt");
            parsed.remove("token");
            parsed.remove("accessToken");
            parsed.remove("refreshToken");

            return parsed;

        } catch (Exception e) {
            return Map.of("raw", payload);
        }
    }
}
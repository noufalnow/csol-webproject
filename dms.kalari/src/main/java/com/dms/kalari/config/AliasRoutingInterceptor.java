package com.dms.kalari.config;

import com.dms.kalari.security.CustomUserPrincipal;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class AliasRoutingInterceptor implements HandlerInterceptor {

    private static final String FORWARDED_FLAG = "X-Alias-Forwarded";
    
    // Define public paths that don't require alias validation
    private static final Set<String> PUBLIC_PATHS = Set.of(
        "/public",
        "/login",
        "/login-error", 
        "/verify",
        "/logout",
        "/error"
    );
    
    // Define public path prefixes
    private static final Set<String> PUBLIC_PATH_PREFIXES = Set.of(
        "/public/",
        "/verify/",
        "/static/",
        "/css/",
        "/js/",
        "/images/",
        "/webjars/"
    );
    
    // Define public file extensions
    private static final Set<String> PUBLIC_FILE_EXTENSIONS = Set.of(
        ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".ico", ".html", ".txt"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath() == null ? "" : request.getContextPath();
        
        log.info("=== ALIAS INTERCEPTOR TRIGGERED ===");
        log.info("Request URI: {}", uri);
        log.info("Context Path: {}", contextPath);
        log.info("Handler Type: {}", handler.getClass().getSimpleName());
        


        // Check if already forwarded
        if (request.getAttribute(FORWARDED_FLAG) != null) {
            log.debug("Request already forwarded, skipping interceptor");
            return true;
        }

        // Check if this is a public path that should be permitted without alias validation
        if (isPublicPath(uri)) {
            log.debug("Public path, skipping alias validation: {}", uri);
            return true;
        }
        

        // For all other paths, require alias validation
        if (!uri.startsWith(contextPath + "/")) {
            log.warn("URI does not start with context path, access denied: {}", uri);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return false;
        }

        String path = uri.substring(contextPath.length()); // starts with '/'
        if (path.length() < 2) {
            log.warn("Path too short to contain alias, access denied: {}", path);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return false;
        }

        int nextSlash = path.indexOf('/', 1);
        String alias = (nextSlash == -1) ? path.substring(1) : path.substring(1, nextSlash);
        log.info("Extracted alias from path: '{}'", alias);

        // Check authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            log.warn("User not authenticated, redirecting to login for: {}", uri);
            response.sendRedirect("/login?error=not_authenticated");
            return false;
        }
        
        log.info("Authentication: {}", auth);
        log.info("Principal class: {}", auth.getPrincipal().getClass().getName());

        if (!(auth.getPrincipal() instanceof CustomUserPrincipal user)) {
            log.warn("Principal is not CustomUserPrincipal, access denied: {}", auth.getPrincipal().getClass());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return false;
        }

        log.info("Authenticated user: {}, userId: {}", user.getUsername(), user.getUserId());

        // Check if user has the required alias privilege
        if (!user.hasAlias(alias)) {
            log.warn("Alias '{}' not found in user's privileges, access denied for user: {}", alias, user.getUsername());
            /***** response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied - No privilege for alias: " + alias); 
            return false; ******/ 
            return true;
        }

        Optional<String> real = user.resolveRealPath(alias);
        if (real.isEmpty()) {
            log.warn("Alias '{}' mapped but real path not found, access denied", alias);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied - Invalid alias mapping");
            return false;
        }

        String actual = real.get();
        String suffix = (nextSlash != -1) ? path.substring(nextSlash) : "";
        String finalPath = actual + suffix;

        if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
            finalPath = finalPath + "?" + request.getQueryString();
        }

        log.info("Forwarding request: {} -> {}", uri, finalPath);

        request.setAttribute(FORWARDED_FLAG, Boolean.TRUE);
        RequestDispatcher rd = request.getRequestDispatcher(finalPath);
        rd.forward(request, response);
        return false;
    }

    private boolean isPublicPath(String uri) {
        // Exact public paths
        if (PUBLIC_PATHS.contains(uri)) {
            return true;
        }
        
        // Public path prefixes
        for (String prefix : PUBLIC_PATH_PREFIXES) {
            if (uri.startsWith(prefix)) {
                return true;
            }
        }
        
        // Public file extensions
        for (String extension : PUBLIC_FILE_EXTENSIONS) {
            if (uri.endsWith(extension)) {
                return true;
            }
        }
        
        // Root path
        if (uri.equals("/") || uri.equals("")) {
            return true;
        }
        
        return false;
    }
}
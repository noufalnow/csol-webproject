package com.dms.kalari.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.dms.kalari.admin.service.AuthLogActionService;
import com.dms.kalari.admin.service.AuthLoginLogService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component("privilegeChecker")
public class PrivilegeChecker {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegeChecker.class);

    private final AuthLoginLogService authLoginLogService;
    private final AuthLogActionService authLogActionService;

    private final HttpServletRequest request;

    @Autowired
    public PrivilegeChecker(
            AuthLoginLogService authLoginLogService,
            AuthLogActionService authLogActionService,
            HttpServletRequest request
    ) {
        this.authLoginLogService = authLoginLogService;
        this.authLogActionService = authLogActionService;
        this.request = request;
    }

    // Record for optimized alias mapping results
    public record AliasMapping(String alias, String realPath) {}

    public boolean hasAccess(String requestUri) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = null;
        Long loginId = null;

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserPrincipal u) {
            user = u;
            loginId = u.getLoginId();

            // If we have an authenticated user but no loginId yet, create it once here
            if (loginId == null) {
                try {
                    Long newLoginId = authLoginLogService.addLoginLog(u.getUserId(), request);
                    u.setLoginId(newLoginId);
                    loginId = newLoginId;
                } catch (Exception ex) {
                    logger.warn("Failed to create login log for user {}: {}", u.getUserId(), ex.getMessage());
                }
            }
        }

        Optional<String> potentialAlias = extractAliasFromPath(requestUri);
        boolean allowed;

        if (potentialAlias.isPresent()) {
            allowed = hasAccessByAlias(potentialAlias.get());
        } else {
            logger.debug("Denied access for resource: {} - no valid alias found", requestUri);
            allowed = false;
        }

        // build a small copy of request parameters to log (avoid large bodies)
        Map<String, String[]> paramMap = Collections.emptyMap();
        try {
            paramMap = request.getParameterMap();
        } catch (Exception e) {
            // ignore
        }
        
        String method = request.getMethod();

        // PRE action log (request snapshot + decision)
        try {
        	authLogActionService.logPreAction(loginId, requestUri, method, paramMap, allowed);
        } catch (Exception e) {
            logger.warn("Failed to write PRE action log for uri {}: {}", requestUri, e.getMessage());
        }

        // If access granted → allow later post-state log by services
        if (allowed) {
            // Example usage in services (after DB save):
            // authLogActionService.logPostAction(loginId, requestUri, savedEntity);
        	//authLogActionService.logPostAction(loginId, requestUri, method, savedEntity);

        } else {
            logger.debug("Denied access for resource: {} (alias: {})", requestUri, potentialAlias.orElse("<none>"));
        }

        return allowed;
    }



    // KEEP this method for other uses (like in checkAccessAndGetMapping)
    // but REMOVE the call from hasAccess() above
    private boolean isPublicResource(String path) {
        String cleanPath = stripQueryParams(path);
        
        return cleanPath.startsWith("/public/") || 
               cleanPath.equals("/login") || 
               cleanPath.equals("/logout") || 
               cleanPath.startsWith("/login/") ||
               cleanPath.equals("/error") ||
               cleanPath.equals("/access-denied") ||
               cleanPath.startsWith("/access-denied/") ||
               cleanPath.startsWith("/verify/") ||
               cleanPath.startsWith("/css/") ||
               cleanPath.startsWith("/js/") ||
               cleanPath.startsWith("/images/") ||
               cleanPath.startsWith("/webjars/") ||
               cleanPath.startsWith("/static/") ||
               cleanPath.startsWith("/assets/") ||
               cleanPath.equals("/favicon.ico") ||
               cleanPath.equals("/health") ||
               cleanPath.equals("/actuator/health") ||
               cleanPath.startsWith("/actuator/info");
    }

    private Optional<String> extractAliasFromPath(String path) {
        if (path == null || path.length() <= 1 || path.equals("/")) {
            return Optional.empty();
        }
        
        String cleanPath = stripQueryParams(path);
        
        String pathWithoutSlash = cleanPath.startsWith("/") ? cleanPath.substring(1) : cleanPath;
        int nextSlash = pathWithoutSlash.indexOf('/');
        
        String potentialAlias = (nextSlash == -1) ? pathWithoutSlash : pathWithoutSlash.substring(0, nextSlash);
        
        if (potentialAlias.length() >= 2 && potentialAlias.matches("[a-zA-Z0-9_]+")) {
            return Optional.of(potentialAlias);
        }
        
        return Optional.empty();
    }

    public boolean hasAccessByAlias(String alias) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            logger.debug("Denying access for alias '{}' due to unauthenticated user", alias);
            return false;
        }

        if (auth.getPrincipal() instanceof CustomUserPrincipal user) {
            boolean hasAccess = user.hasPrivilege(alias);
            if (!hasAccess) {
                logger.debug("User authenticated but denied access for alias '{}'", alias);
            } else {
                logger.debug("Granted access for alias '{}'", alias);
            }
            return hasAccess;
        }
        
        logger.debug("Denying access for alias '{}' - principal is not CustomUserPrincipal", alias);
        return false;
    }

    private String stripQueryParams(String fullPath) {
        int queryIndex = fullPath.indexOf('?');
        return queryIndex == -1 ? fullPath : fullPath.substring(0, queryIndex);
    }

    public Optional<String> getRealPathForAlias(String alias) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserPrincipal user) {
            Optional<String> realPath = user.getRealPath(alias);
            if (realPath.isEmpty()) {
                logger.debug("No real path found for alias: {}", alias);
            }
            return realPath;
        }
        return Optional.empty();
    }

    /**
     * OPTIMIZED: Single method to check access AND get real path mapping
     */
    public Optional<AliasMapping> checkAccessAndGetMapping(String requestUri) {
        if (isPublicResource(requestUri)) {
            return Optional.of(new AliasMapping("", requestUri));
        }
        
        Optional<String> aliasOpt = extractAliasFromPath(requestUri);
        if (aliasOpt.isEmpty()) {
            return Optional.empty();
        }
        
        String alias = aliasOpt.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof CustomUserPrincipal user)) {
            return Optional.empty();
        }
        
        // Single lookup to check both access and get mapping
        String realPath = user.getAliasMappings().get(alias);
        if (realPath == null) {
            return Optional.empty();
        }
        
        return Optional.of(new AliasMapping(alias, realPath));
    }

    /**
     * Transform alias URL to real path with access checking
     */
    public Optional<String> transformAliasToRealPath(String requestUri) {
        if (isPublicResource(requestUri)) {
            return Optional.of(requestUri);
        }
        
        Optional<String> alias = extractAliasFromPath(requestUri);
        if (alias.isPresent() && hasAccessByAlias(alias.get())) {
            return transformAliasToRealPathUnchecked(requestUri, alias.get());
        }
        
        return Optional.empty();
    }

    /**
     * Transform alias to real path WITHOUT access checking
     * Use this when access has already been verified by security filter
     */
    public Optional<String> transformAliasToRealPathUnchecked(String requestUri, String knownAlias) {
        Optional<String> realPath = getRealPathForAlias(knownAlias);
        if (realPath.isPresent()) {
            return transformWithAlias(requestUri, knownAlias, realPath.get());
        }
        logger.debug("No real path found for alias: {}", knownAlias);
        return Optional.empty();
    }

    /**
     * Core transformation logic for DB paths without prefix slashes
     */
    private Optional<String> transformWithAlias(String requestUri, String alias, String realPath) {
        // Extract query string
        int queryIndex = requestUri.indexOf('?');
        String pathOnly = queryIndex == -1 ? requestUri : requestUri.substring(0, queryIndex);
        String queryString = queryIndex == -1 ? "" : requestUri.substring(queryIndex);
        
        // Calculate remaining path after alias
        String aliasWithSlash = "/" + alias;
        String remainingPath;
        
        if (pathOnly.equals(aliasWithSlash)) {
            remainingPath = "";
        } else if (pathOnly.startsWith(aliasWithSlash + "/")) {
            remainingPath = pathOnly.substring(aliasWithSlash.length());
        } else {
            logger.debug("Path '{}' doesn't match expected alias pattern for '{}'", pathOnly, alias);
            return Optional.empty();
        }
        
        // Build final path - DB realPath has no prefix slash, so we add it
        String transformedPath;
        if (remainingPath.isEmpty()) {
            transformedPath = "/" + realPath;
        } else {
            transformedPath = "/" + realPath + remainingPath;
        }
        
        // Clean up any double slashes
        transformedPath = transformedPath.replaceAll("//+", "/");
        
        return Optional.of(transformedPath + queryString);
    }

    /**
     * Utility method to get alias for a given real path (reverse lookup)
     */
    public Optional<String> getAliasForRealPath(String realPath) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserPrincipal user) {
            String cleanRealPath = realPath.startsWith("/") ? realPath.substring(1) : realPath;
            
            return user.getPrivileges().stream()
                    .filter(alias -> {
                        Optional<String> storedPath = user.getRealPath(alias);
                        return storedPath.isPresent() && storedPath.get().equals(cleanRealPath);
                    })
                    .findFirst();
        }
        return Optional.empty();
    }
}
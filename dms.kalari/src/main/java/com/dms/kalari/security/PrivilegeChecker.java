package com.dms.kalari.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher; // Add this import

import java.util.Optional;

@Component("privilegeChecker")
public class PrivilegeChecker {

    private final AntPathMatcher pathMatcher = new AntPathMatcher(); // Add this line

    public boolean hasAccess(String requestUri, String method) {
        // First, check if this is a public resource that should always be permitted
        if (isPublicResource(requestUri)) {
            return true;
        }
        
        // Check direct path access (for endpoints like /admin/users)
        if (hasAccessByPath(requestUri, method)) {
            return true;
        }
        
        // Check alias access (extract potential alias from the path)
        Optional<String> potentialAlias = extractAliasFromPath(requestUri);
        if (potentialAlias.isPresent() && hasAccessByAlias(potentialAlias.get(), method)) {
            return true;
        }
        
        // Default: DENY access
        return false;
    }

    private boolean isPublicResource(String path) {
        return path.startsWith("/public/") || 
               path.equals("/login") || 
               path.startsWith("/login") ||
               path.equals("/error") ||
               path.equals("/access-denied") ||
               path.startsWith("/verify/") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/webjars/") ||
               path.startsWith("/static/") ||
               path.startsWith("/assets/") ||
               path.equals("/favicon.ico");
    }

    private Optional<String> extractAliasFromPath(String path) {
        if (path == null || path.length() <= 1 || path.equals("/")) {
            return Optional.empty();
        }
        
        // Skip known public paths that shouldn't be treated as aliases
        if (path.startsWith("/access-denied") || 
            path.startsWith("/error") || 
            path.startsWith("/login")) {
            return Optional.empty();
        }
        
        // Remove leading slash and get first path segment
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        int nextSlash = cleanPath.indexOf('/');
        
        String potentialAlias = (nextSlash == -1) ? cleanPath : cleanPath.substring(0, nextSlash);
        
        // Only return if it looks like a valid alias (not numeric, not too short)
        if (potentialAlias.length() >= 3 && !potentialAlias.matches("\\d+")) {
            return Optional.of(potentialAlias);
        }
        
        return Optional.empty();
    }

    // Check access by ALIAS (for dynamic controllers)
    public boolean hasAccessByAlias(String alias, String method) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        // Allow only DB-configured users (CustomUserPrincipal)
        if (auth.getPrincipal() instanceof CustomUserPrincipal user) {
            return user.hasPrivilege(alias);
        }

        // Deny for any other type of principal
        return false;
    }

    // Check access by REAL PATH (for static controllers)
    public boolean hasAccessByPath(String fullPath, String method) {
        String path = stripQueryParams(fullPath);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        if (auth.getPrincipal() instanceof CustomUserPrincipal user) {
            return user.getPrivileges().stream()
                    .map(alias -> user.getRealPath(alias).orElse(""))
                    .anyMatch(realPath -> {
                        // For security checking, add /** to all paths for pattern matching
                        String securityPattern = realPath.endsWith("/") ? realPath + "**" : realPath + "/**";
                        return pathMatcher.match(securityPattern, path) || 
                               pathMatcher.match(realPath, path);
                    });
        }
        return false;
    }

    private String stripQueryParams(String fullPath) {
        int queryIndex = fullPath.indexOf('?');
        return queryIndex == -1 ? fullPath : fullPath.substring(0, queryIndex);
    }

    public boolean hasPrivilege(String alias) {
        return hasAccessByAlias(alias, "GET");
    }

    public Optional<String> getRealPathForAlias(String alias) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserPrincipal user) {
            return user.getRealPath(alias);
        }
        return Optional.empty();
    }

    public Optional<String> getAliasForRealPath(String realPath) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserPrincipal user) {
            return user.getPrivileges().stream()
                    .filter(alias -> {
                        String storedPath = user.getRealPath(alias).orElse("");
                        // Use AntPathMatcher for reverse lookup too
                        return pathMatcher.match(storedPath, realPath);
                    })
                    .findFirst();
        }
        return Optional.empty();
    }
}
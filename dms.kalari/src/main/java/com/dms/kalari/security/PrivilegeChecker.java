package com.dms.kalari.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("privilegeChecker")
public class PrivilegeChecker {

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
        
        // Remove leading slash and get first path segment
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        int nextSlash = cleanPath.indexOf('/');
        
        if (nextSlash == -1) {
            return Optional.of(cleanPath);
        } else {
            return Optional.of(cleanPath.substring(0, nextSlash));
        }
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
    public boolean hasAccessByPath(String path, String method) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        
        // Only CustomUserPrincipal has the real path mapping
        if (auth.getPrincipal() instanceof CustomUserPrincipal user) {
            return user.getPrivileges().stream()
                    .map(alias -> user.getRealPath(alias).orElse(""))
                    .anyMatch(realPath -> realPath.equals(path));
        }
        
        // Deny for standard Spring Security users
        return false;
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
                    .filter(alias -> user.getRealPath(alias).orElse("").equals(realPath))
                    .findFirst();
        }
        return Optional.empty();
    }
}
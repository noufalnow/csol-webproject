package com.dms.kalari.controller;

import com.dms.kalari.security.PrivilegeChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/{alias:^(?!css|js|images|webjars|static|assets|favicon\\.ico|public|error|logout|login).+}")
public class DynamicAliasController {

    private static final Logger logger = LoggerFactory.getLogger(DynamicAliasController.class);
    private final PrivilegeChecker privilegeChecker;

    public DynamicAliasController(PrivilegeChecker privilegeChecker) {
        this.privilegeChecker = privilegeChecker;
    }

    @GetMapping("/**")
    public String handleGet(@PathVariable String alias, HttpServletRequest request, HttpServletResponse response) {
        return handleRequest(alias, request, response);
    }

    @PostMapping("/**")
    public String handlePost(@PathVariable String alias, HttpServletRequest request, HttpServletResponse response) {
        return handleRequest(alias, request, response);
    }

    @PutMapping("/**")
    public String handlePut(@PathVariable String alias, HttpServletRequest request, HttpServletResponse response) {
        return handleRequest(alias, request, response);
    }

    @DeleteMapping("/**")
    public String handleDelete(@PathVariable String alias, HttpServletRequest request, HttpServletResponse response) {
        return handleRequest(alias, request, response);
    }

    private String handleRequest(String alias, HttpServletRequest request, HttpServletResponse response) {
        try {
            // OPTIMIZED: Use single method call to check access and get mapping
            Optional<PrivilegeChecker.AliasMapping> mapping = 
                privilegeChecker.checkAccessAndGetMapping(request.getRequestURI());
            
            if (mapping.isPresent()) {
                String finalPath = transformWithAlias(
                    request.getRequestURI(), 
                    mapping.get().alias(), 
                    mapping.get().realPath()
                ).orElse(null);
                
                if (finalPath == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return null;
                }
                
                // Prevent recursive forwarding
                if (isRecursiveForward(finalPath) || finalPath.equals(request.getRequestURI())) {
                    logger.warn("Recursive forward detected from {} to {}", request.getRequestURI(), finalPath);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }
                
                logger.debug("Forwarding {} -> {}", request.getRequestURI(), finalPath);
                return "forward:" + finalPath;
            } else {
                logger.debug("No transformation found for alias: {}", alias);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Alias not found: " + alias);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error processing alias request: {}", alias, e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Processing error");
            } catch (Exception ex) {
                logger.error("Failed to send error response", ex);
            }
            return null;
        }
    }

    /**
     * Core transformation logic (moved from PrivilegeChecker for optimization)
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

    private boolean isRecursiveForward(String path) {
        // Prevent forwarding to paths that might cause infinite loops
        return path.contains("/forward/") || path.contains("/alias/") || path.contains("//");
    }

    /**
     * Utility method to extract remaining path (if needed elsewhere)
     */
    private String extractPathAfterAlias(HttpServletRequest request, String alias) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        // Remove context path
        String remainingPath = path.substring(contextPath.length());
        String aliasPrefix = "/" + alias;
        
        if (remainingPath.startsWith(aliasPrefix)) {
            remainingPath = remainingPath.substring(aliasPrefix.length());
            
            // Ensure proper path format
            if (remainingPath.isEmpty()) {
                return "";
            }
            if (!remainingPath.startsWith("/")) {
                remainingPath = "/" + remainingPath;
            }
        }
        
        return remainingPath;
    }
}
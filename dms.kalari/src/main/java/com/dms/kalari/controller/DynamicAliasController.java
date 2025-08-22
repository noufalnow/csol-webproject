package com.dms.kalari.controller;

import com.dms.kalari.security.PrivilegeChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/{alias:^(?!css|js|images|webjars|static|assets|favicon\\.ico|public|error).+}")
public class DynamicAliasController {

    private final PrivilegeChecker privilegeChecker;

    public DynamicAliasController(PrivilegeChecker privilegeChecker) {
        this.privilegeChecker = privilegeChecker;
    }

    private String extractPathAfterAlias(HttpServletRequest request, String alias) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        String remainingPath = path.substring(contextPath.length());
        String aliasPrefix = "/" + alias;
        
        if (remainingPath.startsWith(aliasPrefix)) {
            remainingPath = remainingPath.substring(aliasPrefix.length());
            
            // Ensure we have a proper path format
            if (remainingPath.isEmpty()) {
                return "";
            }
            if (!remainingPath.startsWith("/")) {
                remainingPath = "/" + remainingPath;
            }
        }
        
        return remainingPath;
    }

    private boolean isRecursiveForward(String path) {
        // Prevent forwarding to paths that might cause infinite loops
        return path.contains("/forward/") || path.contains("/alias/");
    }

    @GetMapping("/**")
    //@PreAuthorize("@privilegeChecker.hasAccessByAlias(#alias, 'GET')")
    public void handleGet(@PathVariable String alias, HttpServletRequest request, HttpServletResponse response) throws Exception {
        handleRequest(alias, request, response);
    }

    @PostMapping("/**")
    //@PreAuthorize("@privilegeChecker.hasAccessByAlias(#alias, 'POST')")
    public void handlePost(@PathVariable String alias, HttpServletRequest request, HttpServletResponse response) throws Exception {
        handleRequest(alias, request, response);
    }

    @PutMapping("/**")
    //@PreAuthorize("@privilegeChecker.hasAccessByAlias(#alias, 'PUT')")
    public void handlePut(@PathVariable String alias, HttpServletRequest request, HttpServletResponse response) throws Exception {
        handleRequest(alias, request, response);
    }

    @DeleteMapping("/**")
    //@PreAuthorize("@privilegeChecker.hasAccessByAlias(#alias, 'DELETE')")
    public void handleDelete(@PathVariable String alias, HttpServletRequest request, HttpServletResponse response) throws Exception {
        handleRequest(alias, request, response);
    }

    private void handleRequest(String alias, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<String> realPathOpt = privilegeChecker.getRealPathForAlias(alias);
        
        if (realPathOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Alias not found: " + alias);
            return;
        }
        
        String realPath = realPathOpt.get();
        String remainingPath = extractPathAfterAlias(request, alias);
        String finalPath = realPath + remainingPath;
        
        // Prevent recursive forwarding
        if (isRecursiveForward(finalPath)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid forward path");
            return;
        }
        
        // Check if the final path is the same as current to prevent loops
        if (finalPath.equals(request.getRequestURI())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Recursive forward detected");
            return;
        }
        
        if (request.getQueryString() != null) {
            finalPath += "?" + request.getQueryString();
        }
        
        request.getRequestDispatcher(finalPath).forward(request, response);
    }
}
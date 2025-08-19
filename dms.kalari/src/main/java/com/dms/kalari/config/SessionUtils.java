package com.dms.kalari.config;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionUtils {
    
    public long getRequiredSessionAttribute(HttpSession session, String attrName) {
        Object value = session.getAttribute(attrName);
        
        
        if (session == null) {
            throw new IllegalStateException("No active session");
        }
        
        
        if (value == null) {
            throw new IllegalStateException("Required session attribute '" + attrName + "' not found");
        }
        try {
            return ((Number) value).longValue();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Session attribute '" + attrName + "' must be a number", e);
        }
    }
}


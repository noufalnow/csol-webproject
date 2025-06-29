package com.example.tenant_service.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class SessionDebugHelper {
    private static final Logger logger = LoggerFactory.getLogger(SessionDebugHelper.class);
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Private constructor to prevent instantiation
    private SessionDebugHelper() {}

    /**
     * Prints all session attributes to log in pretty JSON format
     */
    public static void logAllSessionAttributes(HttpSession session) {
        if (session == null) {
            logger.warn("Session is null");
            return;
        }

        logger.info("===== SESSION ATTRIBUTES (ID: {}) =====", session.getId());
        try {
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                Object value = session.getAttribute(name);
                logSessionAttribute(name, value);
            }
        } catch (Exception e) {
            logger.error("Error logging session attributes", e);
        }
        logger.info("===== END SESSION ATTRIBUTES =====");
    }

    /**
     * Returns a map of all session attributes for inspection
     */
    public static Map<String, Object> getSessionAttributesMap(HttpSession session) {
        Map<String, Object> attributes = new LinkedHashMap<>();
        if (session == null) {
            return attributes;
        }

        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            attributes.put(name, session.getAttribute(name));
        }
        return attributes;
    }

    /**
     * Logs a single session attribute with type information
     */
    private static void logSessionAttribute(String name, Object value) {
        try {
            logger.info("Attribute: {}", name);
            logger.info("Type: {}", value != null ? value.getClass().getName() : "null");
            
            if (value != null) {
                if (isSimpleType(value)) {
                    logger.info("Value: {}", value);
                } else {
                    logger.info("Value:\n{}", mapper.writeValueAsString(value));
                }
            } else {
                logger.info("Value: null");
            }
            logger.info("----------------------");
        } catch (Exception e) {
            logger.error("Error logging session attribute: {}", name, e);
        }
    }

    /**
     * Checks if the value is a simple type that doesn't need JSON formatting
     */
    private static boolean isSimpleType(Object value) {
        return value instanceof String || 
               value instanceof Number || 
               value instanceof Boolean ||
               value instanceof Enum;
    }
}
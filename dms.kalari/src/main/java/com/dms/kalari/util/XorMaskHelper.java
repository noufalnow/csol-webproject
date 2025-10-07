package com.dms.kalari.util;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * A strictly session-based XOR masking helper.
 * Uses only the "sessionKey" stored in HttpSession — no fallbacks, no randoms.
 */
@Component
@SessionScope
public class XorMaskHelper {

    private final HttpSession httpSession;

    public XorMaskHelper(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Initialize a session key explicitly.
     * Must be called once after login.
     */
    public void initializeSessionKey(long key) {
        if (key == 0L) {
            throw new IllegalArgumentException("Session key cannot be zero");
        }
        httpSession.setAttribute("sessionKey", key);
    }

    /**
     * Retrieve the session key.
     */
    public long getSessionKey() {
        Long key = (Long) httpSession.getAttribute("sessionKey");
        if (key == null) {
            throw new IllegalStateException("Session key not initialized for this session");
        }
        return key;
    }

    @PreDestroy
    public void cleanup() {
        httpSession.removeAttribute("sessionKey");
    }

    // ===================== STATIC UTILITIES =====================

    /**
     * Mask a value using the key from the current session.
     */
    public static long mask(long value) {
        long key = getCurrentSessionKey();
        return value ^ key;
    }

    /**
     * Unmask a value using the key from the current session.
     */
    public static long unmask(long maskedValue) {
        long key = getCurrentSessionKey();
        return maskedValue ^ key;
    }

    /**
     * Mask/unmask using a provided key.
     */
    public static long mask(long value, long key) {
        return value ^ key;
    }

    public static long unmask(long maskedValue, long key) {
        return maskedValue ^ key;
    }

    /**
     * Helper: Get current session's key (no fallbacks).
     */
    private static long getCurrentSessionKey() {
        HttpSession session = getCurrentSession();
        if (session == null) {
            throw new IllegalStateException("No active HTTP session found");
        }

        Long key = (Long) session.getAttribute("sessionKey");
        if (key == null) {
            throw new IllegalStateException("Session key not found in current session");
        }
        return key;
    }

    /**
     * Helper: Get the active HTTP session.
     */
    private static HttpSession getCurrentSession() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("No request context available");
        }
        return attrs.getRequest().getSession(false); // don't create new
    }
}

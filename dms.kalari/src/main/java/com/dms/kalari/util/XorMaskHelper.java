package com.dms.kalari.util;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@SessionScope
public class XorMaskHelper {

    private static final long DEFAULT_TEST_KEY = 569874L;
    private static final SecureRandom RANDOM = new SecureRandom();

    // ThreadLocal cache for fast access within the same thread/request
    private static final ThreadLocal<Long> sessionKeyHolder = new ThreadLocal<>();

    // Map sessionId -> sessionKey for reference (optional)
    private static final ConcurrentHashMap<String, Long> sessionKeyMap = new ConcurrentHashMap<>();

    private final HttpSession httpSession;

    // Instance fields
    private long sessionKey;
    private boolean keyInitialized = false;

    public XorMaskHelper(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    // ================= INSTANCE METHODS =================

    /**
     * Initialize session key explicitly
     */
    public void initializeSessionKey(long key) {
        if (key == 0L) throw new IllegalArgumentException("Session key cannot be zero");
        this.sessionKey = key;
        this.keyInitialized = true;

        httpSession.setAttribute("sessionKey", key);
        sessionKeyMap.put(httpSession.getId(), key);
        sessionKeyHolder.set(key); // cache for current thread
    }

    /**
     * Initialize a random non-zero session key
     */
    public void initializeWithRandomKey() {
        long key;
        do {
            key = RANDOM.nextLong();
        } while (key == 0L);
        initializeSessionKey(key);
    }

    /**
     * Get the instance session key
     */
    public long getSessionKey() {
        if (!keyInitialized) initializeWithRandomKey();
        return sessionKey;
    }

    @PreDestroy
    public void cleanup() {
        sessionKeyMap.remove(httpSession.getId());
    }

    // ================= STATIC METHODS =================

    /**
     * Mask value using ThreadLocal first, then session, fallback to default key
     */
    public static long mask(long value) {
        Long threadKey = sessionKeyHolder.get();
        if (threadKey != null) {
            return value ^ threadKey;
        }
        return value ^ getEffectiveSessionKey();
    }

    /**
     * Unmask value using ThreadLocal first, then session, fallback to default key
     */
    public static long unmask(long maskedValue) {
        Long threadKey = sessionKeyHolder.get();
        if (threadKey != null) {
            return maskedValue ^ threadKey;
        }
        return maskedValue ^ getEffectiveSessionKey();
    }

    /**
     * Get the session-aware key for static calls
     */
    private static long getEffectiveSessionKey() {
        // Try ThreadLocal first
        Long cachedKey = sessionKeyHolder.get();
        if (cachedKey != null) return cachedKey;

        try {
            HttpSession session = getCurrentSession();
            if (session != null) {
                Long key = (Long) session.getAttribute("sessionKey");
                if (key != null) {
                    sessionKeyHolder.set(key); // cache in ThreadLocal
                    return key;
                }
            }
        } catch (Exception ignored) {
        }

        // fallback
        return DEFAULT_TEST_KEY;
    }

    /**
     * Get current HTTP session from RequestContextHolder
     */
    private static HttpSession getCurrentSession() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            return attrs.getRequest().getSession(false); // don't create new session
        }
        return null;
    }

    /**
     * Mask/unmask using a specific key directly
     */
    public static long mask(long value, long key) {
        return value ^ key;
    }

    public static long unmask(long maskedValue, long key) {
        return maskedValue ^ key;
    }

    /**
     * Generate a random non-zero key
     */
    public static long generateSessionKey() {
        long key;
        do {
            key = RANDOM.nextLong();
        } while (key == 0L);
        return key;
    }
}

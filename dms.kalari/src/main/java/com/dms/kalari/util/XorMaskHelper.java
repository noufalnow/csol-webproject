package com.dms.kalari.util;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpSession;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class XorMaskHelper {
    private static final long DEFAULT_TEST_KEY = 569874L;
    private static final SecureRandom RANDOM = new SecureRandom();
    
    // Thread-local storage for session key to support static methods
    private static final ThreadLocal<Long> sessionKeyHolder = new ThreadLocal<>();
    private static final ConcurrentHashMap<String, Long> sessionKeyMap = new ConcurrentHashMap<>();
    
    private String sessionId;
    private long sessionKey;
    private boolean keyInitialized = false;

    // Inject HttpSession to get automatic session ID
    private final HttpSession httpSession;

    public XorMaskHelper(HttpSession httpSession) {
        this.httpSession = httpSession;
        this.sessionId = httpSession.getId();
    }

    /**
     * Initialize the session key once upon login
     */
    public void initializeSessionKey(long key) {
        if (key == 0L) {
            throw new IllegalArgumentException("Session key cannot be zero");
        }
        this.sessionKey = key;
        this.keyInitialized = true;
        
        // Store in global map for static access
        sessionKeyMap.put(this.sessionId, key);
    }

    /**
     * Initialize with a randomly generated session key
     */
    public void initializeWithRandomKey() {
        long key;
        do {
            key = RANDOM.nextLong();
        } while (key == 0L);
        initializeSessionKey(key);
    }

    /**
     * Check if the session key has been initialized
     */
    public boolean isKeyInitialized() {
        return keyInitialized;
    }

    /**
     * Get the current session key
     */
    public long getSessionKey() {
        if (!keyInitialized) {
            initializeWithRandomKey();
        }
        return sessionKey;
    }

    /**
     * Mask a value with the session key (instance method)
     */
    /*public long mask(long value) {
        return value ^ getSessionKey();
    }*/

    /**
     * Unmask a previously masked value with the session key (instance method)
     */
    /*public long unmask(long maskedValue) {
        return maskedValue ^ getSessionKey();
    }*/

    /**
     * Clean up when session is destroyed
     */
    @PreDestroy
    public void cleanup() {
        sessionKeyMap.remove(sessionId);
    }

    // =============== STATIC METHODS ===============

    /**
     * Set the current thread's session key for static method calls
     */
    public static void setThreadSessionKey(long key) {
        sessionKeyHolder.set(key);
    }

    /**
     * Set the current thread's session key by session ID for static method calls
     */
    public static void setThreadSessionKey(String sessionId) {
        Long key = sessionKeyMap.get(sessionId);
        if (key != null) {
            sessionKeyHolder.set(key);
        } else {
            throw new IllegalStateException("No session key found for session ID: " + sessionId);
        }
    }

    /**
     * Clear the current thread's session key
     */
    public static void clearThreadSessionKey() {
        sessionKeyHolder.remove();
    }

    /**
     * Get the current thread's session key or null if not set
     */
    public static Long getCurrentThreadSessionKey() {
        return sessionKeyHolder.get();
    }

    /**
     * Mask a value with the current thread's session key (static method)
     */
    public static long mask(long value) {
        Long sessionKey = sessionKeyHolder.get();
        if (sessionKey != null) {
            return value ^ sessionKey;
        }
        return mask(value, DEFAULT_TEST_KEY);
    }

    /**
     * Unmask a previously masked value with the current thread's session key (static method)
     */
    public static long unmask(long maskedValue) {
        Long sessionKey = sessionKeyHolder.get();
        if (sessionKey != null) {
            return maskedValue ^ sessionKey;
        }
        return unmask(maskedValue, DEFAULT_TEST_KEY);
    }

    /**
     * Mask with a specific key (static method)
     */
    public static long mask(long value, long key) {
        return value ^ key;
    }

    /**
     * Unmask with a specific key (static method)
     */
    public static long unmask(long maskedValue, long key) {
        return maskedValue ^ key;
    }

    /**
     * Generate a random non-zero session key (static utility)
     */
    public static long generateSessionKey() {
        long key;
        do {
            key = RANDOM.nextLong();
        } while (key == 0L);
        return key;
    }

    /**
     * Result holder for masked value + key (for backward compatibility)
     */
    public static class MaskResult {
        private final long maskedValue;
        private final long sessionKey;

        public MaskResult(long maskedValue, long sessionKey) {
            this.maskedValue = maskedValue;
            this.sessionKey = sessionKey;
        }

        public long getMaskedValue() {
            return maskedValue;
        }

        public long getSessionKey() {
            return sessionKey;
        }
    }

    /**
     * Convenience: mask with a new random key (static method)
     */
    public static MaskResult maskWithNewKey(long value) {
        long key = generateSessionKey();
        long masked = mask(value, key);
        return new MaskResult(masked, key);
    }
}
package com.dms.kalari.security;

import java.util.Base64;

public final class XorMaskHelper {
    
    private static char MASK_KEY = 'k'; // Default key
    
    // Initialize from system property or environment variable
    static {
        String key = System.getProperty("app.mask.key", 
                      System.getenv().getOrDefault("APP_MASK_KEY", "k"));
        if (key != null && !key.isEmpty()) {
            MASK_KEY = key.charAt(0);
        }
    }
    
    private XorMaskHelper() {} // Prevent instantiation
    
    // Set key programmatically (optional)
    public static void setMaskKey(char key) {
        MASK_KEY = key;
    }
    
    public static char getMaskKey() {
        return MASK_KEY;
    }
    
    // String masking
    public static String mask(String input) {
        if (input == null) return null;
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] ^ MASK_KEY);
        }
        return new String(chars);
    }
    
    // String unmasking (same as masking for XOR)
    public static String unmask(String masked) {
        return mask(masked);
    }
    
    // Integer masking
    public static Integer mask(Integer input) {
        if (input == null) return null;
        return input ^ (int) MASK_KEY;
    }
    
    // Integer unmasking
    public static Integer unmask(Integer masked) {
        return mask(masked);
    }
    
    // Long masking
    public static Long mask(Long input) {
        if (input == null) return null;
        return input ^ (long) MASK_KEY;
    }
    
    // Long unmasking
    public static Long unmask(Long masked) {
        return mask(masked);
    }
    
    // Double masking
    public static Double mask(Double input) {
        if (input == null) return null;
        long longBits = Double.doubleToLongBits(input);
        long maskedBits = longBits ^ (long) MASK_KEY;
        return Double.longBitsToDouble(maskedBits);
    }
    
    // Double unmasking
    public static Double unmask(Double masked) {
        return mask(masked);
    }
    
    // Generic method that detects type
    public static Object mask(Object input) {
        if (input == null) return null;
        
        if (input instanceof String) return mask((String) input);
        if (input instanceof Integer) return mask((Integer) input);
        if (input instanceof Long) return mask((Long) input);
        if (input instanceof Double) return mask((Double) input);
        
        throw new IllegalArgumentException("Unsupported type: " + input.getClass().getSimpleName());
    }
    
    // Generic unmasking
    public static Object unmask(Object masked) {
        return mask(masked);
    }
    
    // Optional: Base64 version for printable output
    public static String maskBase64(String input) {
        byte[] bytes = mask(input).getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static String unmaskBase64(String base64) {
        byte[] bytes = Base64.getDecoder().decode(base64);
        return unmask(new String(bytes));
    }
}
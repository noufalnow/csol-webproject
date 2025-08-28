package com.dms.kalari.util;

import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public final class XorMaskHelper {

    private static char MASK_KEY;
    private static final long MIN_10_DIGIT = 1_000_000_000L; // smallest 10-digit number
    private static final long MAX_9_DIGIT = 999_999_999L;    // largest 9-digit number
    private static final long FLAG_BIT = 1L << 63;           // highest bit used as offset flag

    static {
        String key = System.getProperty("app.mask.key",
                System.getenv().getOrDefault("APP_MASK_KEY", "k"));
        if (key != null && !key.isEmpty()) {
            MASK_KEY = key.charAt(0);
        }
    }

    private XorMaskHelper() {}

    public static void setMaskKey(char key) {
        MASK_KEY = key;
    }

    public static char getMaskKey() {
        return MASK_KEY;
    }

    // ==================== LONG MASK/UNMASK (always 10+ digits) ====================
    public static long mask(long value) {
        long masked = value ^ MASK_KEY;

        if (masked <= MAX_9_DIGIT) {
            // Add offset and mark with flag bit
            return (masked + MIN_10_DIGIT) | FLAG_BIT;
        }
        return masked;
    }

    public static long unmask(long masked) {
        if ((masked & FLAG_BIT) != 0) {
            // Offset was applied → clear flag, subtract offset, then unmask
            return ((masked & ~FLAG_BIT) - MIN_10_DIGIT) ^ MASK_KEY;
        }
        return masked ^ MASK_KEY;
    }

    // ==================== STRING MASK/UNMASK ====================
    public static String smask(String input) {
        if (input == null) return null;
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= MASK_KEY;
        }
        return new String(chars);
    }

    public static String sunmask(String masked) {
        return smask(masked); // XOR is reversible
    }

    // ==================== GENERIC NUMBER MASK ====================
    public static long smask(Number input) {
        if (input == null) return 0L;

        long value;
        if (input instanceof Integer) value = input.longValue();
        else if (input instanceof Long) value = (Long) input;
        else if (input instanceof Double) value = Double.doubleToLongBits((Double) input);
        else throw new IllegalArgumentException("Unsupported type: " + input.getClass().getSimpleName());

        return mask(value);
    }

    public static Number sunmask(long masked, Class<?> targetType) {
        long original = unmask(masked);

        if (targetType == Integer.class) return (int) original;
        if (targetType == Long.class) return original;
        if (targetType == Double.class) return Double.longBitsToDouble(original);

        throw new IllegalArgumentException("Unsupported target type: " + targetType.getSimpleName());
    }

    // ==================== BASE64 HELPERS ====================
    public static String smaskBase64(String input) {
        if (input == null) return null;
        return Base64.getEncoder().encodeToString(smask(input).getBytes());
    }

    public static String sunmaskBase64(String base64) {
        if (base64 == null) return null;
        byte[] bytes = Base64.getDecoder().decode(base64);
        return sunmask(new String(bytes));
    }

    // ==================== FORMATTED STRING OUTPUT ====================
    public static String maskTo10DigitString(long value) {
        long masked = mask(value);
        return String.format("%010d", masked & ~FLAG_BIT); // clear flag for display
    }
}

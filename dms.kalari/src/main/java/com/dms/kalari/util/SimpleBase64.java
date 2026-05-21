package com.dms.kalari.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SimpleBase64 {

    public static String encode(
            long id,
            String filename) {

        String raw =
                id + "|" + filename;

        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        raw.getBytes(
                                StandardCharsets.UTF_8
                        )
                );
    }

    public static String decode(
            String token) {

        byte[] decoded =
                Base64
                .getUrlDecoder()
                .decode(token);

        return new String(
                decoded,
                StandardCharsets.UTF_8
        );
    }

}
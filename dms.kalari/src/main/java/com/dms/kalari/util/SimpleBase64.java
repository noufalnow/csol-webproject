package com.dms.kalari.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SimpleBase64 {
	public static String encode(long id, String filename) {
		String raw = id + "|" + filename;
		return Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
	}

	public static String decode(String token) {
		byte[] decoded = Base64.getUrlDecoder().decode(token);
		String raw = new String(decoded, StandardCharsets.UTF_8);
		String[] parts = raw.split("\\|", 2);
		long id = Long.parseLong(parts[0]);
		String filename = parts[1];
		return "id=" + id + ", filename=" + filename;
	}

	// Example usage
	/*public static void main(String[] args) {
		String token = encode(94L, "20251009211539800531.pdf");
		System.out.println(token);
		System.out.println(decode(token));
	}*/
}

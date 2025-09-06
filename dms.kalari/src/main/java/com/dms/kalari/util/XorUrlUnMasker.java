package com.dms.kalari.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component

public class XorUrlUnMasker {
	
	private static final Pattern ID_PATTERN = Pattern.compile("/(\\d+)(?=/|$)"); // Slightly more precise

	public static String unmaskUri(String uri) {
	    // Early return for URIs without potential IDs
	    if (uri == null || !uri.contains("/")) {
	        return uri;
	    }
	    
	    try {
	        Matcher matcher = ID_PATTERN.matcher(uri);
	        StringBuffer result = new StringBuffer();

	        while (matcher.find()) {
	            String maskedIdStr = matcher.group(1);
	            try {
	                long maskedId = Long.parseLong(maskedIdStr);
	                long unmaskedId = XorMaskHelper.unmask(maskedId);
	                matcher.appendReplacement(result, "/" + Matcher.quoteReplacement(String.valueOf(unmaskedId)));
	            } catch (NumberFormatException e) {
	                // If not a valid long, leave as-is
	                matcher.appendReplacement(result, "/" + Matcher.quoteReplacement(maskedIdStr));
	            } catch (Exception e) {
	                // Handle other exceptions from unmask()
	                matcher.appendReplacement(result, "/" + Matcher.quoteReplacement(maskedIdStr));
	            }
	        }
	        matcher.appendTail(result);

	        return result.toString();
	    } catch (Exception e) {
	        // Fallback: return original
	        return uri;
	    }
	}
	
	
}
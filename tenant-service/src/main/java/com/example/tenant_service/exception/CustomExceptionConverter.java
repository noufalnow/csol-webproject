package com.example.tenant_service.exception;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;

public class CustomExceptionConverter extends ThrowableProxyConverter {
   
	protected void render(ThrowableProxy tp, ILoggingEvent event, StringBuilder builder) {
	    StackTraceElement[] stackTrace = tp.getThrowable().getStackTrace();
	    
	    for (StackTraceElement element : stackTrace) {
	        String className = element.getClassName();
	        // Exclude Spring, Hibernate, and JDK-related stack traces
	        if (className.startsWith("com.example.tenant_service") 
	                || className.startsWith("java.base")) {
	            builder.append(element.toString()).append(System.lineSeparator());
	        }
	    }
	}


}


package com.dms.kalari.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AliasRoutingInterceptor aliasInterceptor;

    public WebConfig(AliasRoutingInterceptor aliasInterceptor) {
        this.aliasInterceptor = aliasInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(aliasInterceptor)
                .addPathPatterns("/**")  // Explicitly register for ALL paths
                .order(0);               // Set appropriate order (0 = highest priority)
    }
}
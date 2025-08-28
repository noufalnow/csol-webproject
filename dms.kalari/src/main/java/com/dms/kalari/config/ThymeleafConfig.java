package com.dms.kalari.config;

import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.dms.kalari.util.XorMaskHelper;

import jakarta.annotation.PostConstruct;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class ThymeleafConfig {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private XorMaskHelper xorMaskHelper;

    @PostConstruct
    public void configure() {
        templateEngine.addDialect(new org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect());
    }
}


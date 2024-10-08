package com.example.tenant_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.tinylog.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.example.tenant_service"})
public class TenantServiceApplication {
    public static void main(String[] args) {
        // Log messages
        /*Logger.debug("This is a debug message");
        Logger.info("This is an info message");
        Logger.warn("This is a warning message");
        Logger.error("This is an error message");*/
        
        // Run the Spring application
        SpringApplication.run(TenantServiceApplication.class, args);
    }
}

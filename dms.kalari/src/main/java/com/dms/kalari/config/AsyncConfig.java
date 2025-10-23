package com.dms.kalari.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Core pool size - number of threads to keep in the pool
        executor.setCorePoolSize(5);
        
        // Max pool size - maximum number of threads that can be created
        executor.setMaxPoolSize(10);
        
        // Queue capacity - number of tasks that can be queued when all core threads are busy
        executor.setQueueCapacity(50);
        
        // Thread name prefix for better logging
        executor.setThreadNamePrefix("Async-Email-");
        
        // Rejected execution handler - what to do when queue is full
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, java.util.concurrent.ThreadPoolExecutor executor) {
                log.warn("Async task rejected due to full queue and thread pool. Task will be executed in caller thread.");
                // Execute in caller thread as fallback
                if (!executor.isShutdown()) {
                    r.run();
                }
            }
        });
        
        // Important: Initialize the executor
        executor.initialize();
        
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("Async method execution failed: {}.{} with parameters {}",
                    method.getDeclaringClass().getName(), method.getName(), params, ex);
            }
        };
    }
}
package com.example.highlevel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author MLKJ
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean("getTaskExecutor")
    public TaskExecutor taskExecutor() {
        int theadCount = Runtime.getRuntime().availableProcessors();
        System.out.println("当前cpu支持："+theadCount);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(5);
        executor.setAllowCoreThreadTimeOut(false);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
    
}

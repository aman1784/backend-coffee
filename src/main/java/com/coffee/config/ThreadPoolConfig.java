package com.coffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Value("${search.log.thread.pool.size:15}")
    private int logExecutorCorePoolSize;

    @Value("${search.log.thread.max.pool.size:15}")
    private int logExecutorMaxPoolSize;

    @Value("${search.log.thread.keepAliveSeconds:18000}")
    private int logExecutorKeepAliveSeconds;

    @Value("${search.log.thread.queue.capacity:100}")
    private int logExecutorQueueCapacity;

//    @Bean({"threadPoolExecutor"})
//    ThreadPoolTaskExecutor logExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(logExecutorCorePoolSize);
//        executor.setMaxPoolSize(logExecutorMaxPoolSize);
//        executor.setQueueCapacity(logExecutorQueueCapacity);
//        executor.setKeepAliveSeconds(logExecutorKeepAliveSeconds);
//        return executor;
//    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                10, // core pool size
                20, // max pool size
                60, // keep alive time
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );
    }

}

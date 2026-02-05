 package com.coffee.config;

import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class RedisStartupCheck {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @PostConstruct
    public void printRedisConfig() {
        System.out.println(">>> Redis Host: " + redisHost);
        System.out.println(">>> Redis Port: " + redisPort);
    }

}

package com.coffee.config;
//
//import io.lettuce.core.ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
//
//@Configuration
//public class RedisConfig {

//    @Bean
//    public LettuceClientConfiguration lettuceClientConfiguration() {
//        return LettuceClientConfiguration.builder()
//                .clientOptions(ClientOptions.builder().build())
//                .disableTelemetry() // disables sending telemetry (thus avoids SETINFO)
//                .build();
//    }
//
//    @Bean
//    public LettuceClientConfiguration lettuceClientConfiguration() {
//        return LettuceClientConfiguration.builder()
//                .disableTelemetry()
//                .build();
//    }

//}
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(
            @Value("${spring.redis.host}") String host,
            @Value("${spring.redis.port}") int port,
            @Value("${spring.redis.password:}") String password) {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        if (!password.isEmpty()) config.setPassword(password);
//        config.setSsl(true);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Default TTL of 30 minutes
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // ⏱️ Set TTL to 1 hour
                .disableCachingNullValues();      // Optional: avoid caching nulls

        // Optional: Set TTL per cache name
//        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
//        cacheConfigs.put("myCache", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
//                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }

}

package com.coffee.controller;

import com.coffee.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1.0/redis-status")
public class RedisStatusController {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @GetMapping(value = "/checkRedisStatus")
    public ResponseEntity<BaseResponse<Map<String, String>>> checkRedisStatus() {
        log.debug("[ConnectionController][serverStatus] Redis Ping requested on port {}", redisPort);
        BaseResponse<Map<String, String>> response =
                BaseResponse.<Map<String, String>>builder()
                        .data(Map.of("redisStatus", "Redis Successfully Started", "redisHost", redisHost, "redisPort", String.valueOf(redisPort)))
                        .build();
        log.debug("[ConnectionController][serverStatus] Redis Pong response sent");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

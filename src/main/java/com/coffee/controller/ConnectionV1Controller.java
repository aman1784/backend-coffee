package com.coffee.controller;

import com.coffee.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/connections/ping")
@Slf4j
public class ConnectionV1Controller {

    @Value("${server.port}")
    private int port;

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
//
//    @Value("${spring.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//
    @GetMapping("/server")
    public ResponseEntity<BaseResponse<Map<String, String>>> serverStatus() {
        log.debug("[ConnectionController][serverStatus] Server Ping requested on port {}", port);
        BaseResponse<Map<String, String>> response =
                BaseResponse.<Map<String, String>>builder()
                        .data(Map.of("serverStatus", "Server Successfully Started", "port", String.valueOf(port)))
                        .build();
        log.debug("[ConnectionController][serverStatus] Server Pong response sent");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/database")
    public ResponseEntity<BaseResponse<Map<String, String>>> databaseStatus() {
        log.debug("[ConnectionController][databaseStatus] Database Ping request sent");
        try (var conn = DriverManager.getConnection(dataSourceUrl, username, password)) {
            if (conn != null) {
                log.debug("[ConnectionController][databaseStatus] Database connection successful");
            } else {
                log.error("[ConnectionController][databaseStatus] Database connection failed");
                return new ResponseEntity<>(
                        BaseResponse.<Map<String, String>>builder()
                                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("Database Connection Failed")
                                .data(Map.of("databaseStatus", "Database Connection Failed", "databaseError", "Database Connection Failed", "databaseUrl", dataSourceUrl, "username", username, "password", password))
                                .build(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            log.error("[ConnectionController][databaseStatus] Database connection exception", e);
            return new ResponseEntity<>(
                    BaseResponse.<Map<String, String>>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Database Connection Failed")
                            .data(Map.of("databaseStatus", "Database Connection Failed", "databaseError", e.getMessage(), "databaseUrl", dataSourceUrl, "username", username, "password", password))
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BaseResponse<Map<String, String>> response =
                BaseResponse.<Map<String, String>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Database Successfully Connected")
                        .data(Map.of("databaseStatus", "Database Successfully Connected"))
                        .build();
        log.debug("[ConnectionController][databaseStatus] Database Pong response sent");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @GetMapping("/redis")
//    public ResponseEntity<BaseResponse<Map<String, String>>> redisStatus() {
//        log.debug("[ConnectionController][redisStatus] Redis Ping request sent");
//        BaseResponse<Map<String, String>> response =
//                BaseResponse.<Map<String, String>>builder()
//                        .code(HttpStatus.OK.value())
//                        .message("Redis Successfully Connected")
//                        .data(Map.of("redisStatus", "Redis Successfully Connected", "redisHost", redisHost, "redisPort", String.valueOf(redisPort)))
//                        .build();
//        log.debug("[ConnectionController][redisStatus] Redis Pong response sent");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}

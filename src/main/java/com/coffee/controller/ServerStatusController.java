package com.coffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1.0/serverstatus")
public class ServerStatusController {

    private final LoggingSystem loggingSystem;

    @GetMapping(value = {"/checkStatus"})
    public ResponseEntity<String> checkStatus() {
        log.debug("[ServerStatusController][checkStatus] DEBUG {}", "");
        log.info("[ServerStatusController][checkStatus] INFO {}", "");
        log.error("[ServerStatusController][checkStatus] ERROR: {}", "");
        return new ResponseEntity<>("Backend Coffee App Server is up and running", HttpStatus.OK);
    }

//    @GetMapping(path = "/changeLogLevel")
//    public ResponseEntity<String> changeLogLevel(@RequestParam("level") String logLevel) {
//        log.debug("[ServerStatusController][changeLogLevel] Log Level Change Request {}", logLevel);
//        Level newLevel;
//        newLevel = Level.valueOf(logLevel.toUpperCase());
//        LoggerContext context = (LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
//        Configurator.setRootLevel(newLevel);
//        context.updateLoggers();
//
//        return new ResponseEntity<>("Backend App Server Log Level Changes Successfully : "+logLevel, HttpStatus.OK);
//    }

    //    ✅ Notes
    //    - loggerName can be a package (com.example) or a specific class (com.example.MyService).
    //    - Valid levels: TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF.
    //     - This change is in-memory only—if you restart the app, it resets to the config in application.properties.
    @GetMapping("/change-log-level")
    public String changeLogLevel(@RequestParam String loggerName,
                                 @RequestParam String level) {
        try {
            LogLevel logLevel = LogLevel.valueOf(level.toUpperCase());
            loggingSystem.setLogLevel(loggerName, logLevel);
            return "Changed log level of " + loggerName + " to " + logLevel;
        } catch (IllegalArgumentException e) {
            return "Invalid log level: " + level;
        }
    }

    // New endpoint to list all loggers and their levels
    @GetMapping("/list-loggers")
    public List<LoggerConfiguration> listLoggers() {
        return loggingSystem.getLoggerConfigurations();
    }

}

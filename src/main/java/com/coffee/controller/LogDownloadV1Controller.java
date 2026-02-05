package com.coffee.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

@RestController
@RequestMapping(value = "/api/v1.0")
public class LogDownloadV1Controller {

//    private static final String LOG_FILE_PATH = "/var/log/myapp/app.log"; // Replace with actual log file path
    private static final Path LOG_FILE_PATH = Paths.get("D:",  "project coffee", "coffee", "application.log");


    @GetMapping(value = "/coffee/logs/download")
    public ResponseEntity<InputStreamResource> downloadLogsAsGzip() throws IOException {
        // Step 1: Read original log file
        File originalLogFile = new File(LOG_FILE_PATH.toUri());

        if (!originalLogFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Step 2: Create temporary .gz file
        File tempGzipFile = File.createTempFile("log-", ".gz");

        try (
                FileInputStream fis = new FileInputStream(originalLogFile);
                FileOutputStream fos = new FileOutputStream(tempGzipFile);
                GZIPOutputStream gos = new GZIPOutputStream(fos)
        ) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                gos.write(buffer, 0, len);
            }
        }

        // Step 3: Return it as a downloadable stream
        InputStreamResource resource = new InputStreamResource(new FileInputStream(tempGzipFile));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logs.gz")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(tempGzipFile.length())
                .body(resource);
    }
}

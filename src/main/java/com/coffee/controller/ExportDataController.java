package com.coffee.controller;

import com.coffee.repository.CategoryRepository;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping(path = "/api/v1.0/export-data")
@Slf4j
@RequiredArgsConstructor
public class ExportDataController {

    private static final String SECRET_API_KEY = "F984RKJF9UFJjoisuj98urjlk4uA987uiu98uU884RKJF9UFiuhy78TSTWEG8Esuj98urjlk4u";

    private final CategoryRepository categoryRepository;

    @PostMapping(value = "/category")
    public ResponseEntity<String> importCategory(
            @RequestBody CategoryRequest request,
            @RequestHeader("X-API-KEY") String apiKey) {

        // 1. Security Check
        checkApiKey(apiKey);
        saveCategory(request);
        return ResponseEntity.ok("Category saved: " + request.getName());

    }

    @Transactional
    private void saveCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            log.info("Category already exists: {}", request.getName());
            return;
        }

        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        category.setStatus(1);
        category.setCreatedAt(OffsetDateTime.now());
        category.setUpdatedAt(OffsetDateTime.now());

        categoryRepository.save(category);
        log.info("Category saved: {}", request.getName());
    }

    private void checkApiKey(String apiKey) {
        if (apiKey == null || !apiKey.equals(SECRET_API_KEY)) {
            log.info("Unauthorized: Invalid API Key");
            throw new SecurityException("Unauthorized: Invalid API Key");
        }
    }

    @Data
    @Getter
    @Setter
    public static class CategoryRequest {
        private String name;
    }

    @Entity
    @Table(name = "category")
    @Getter
    @Setter
    public static class CategoryEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "status")
        private int status;

        @Column(name = "created_at", updatable = false)
        private OffsetDateTime createdAt;

        @Column(name = "updated_at")
        private OffsetDateTime updatedAt;
    }



}

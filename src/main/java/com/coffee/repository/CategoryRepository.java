package com.coffee.repository;

import com.coffee.controller.ExportDataController;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<ExportDataController.CategoryEntity, Long> {
    Optional<ExportDataController.CategoryEntity> findByName(String name);
}


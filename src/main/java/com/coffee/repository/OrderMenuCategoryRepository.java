package com.coffee.repository;

import com.coffee.entity.OrderMenuCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuCategoryRepository extends JpaRepository<OrderMenuCategoryEntity, Long> {

    List<OrderMenuCategoryEntity> findAllByStatus(int status);

    @Query(value = "SELECT * FROM menu_categories " +
            "WHERE (LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(category_description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND status = 1", nativeQuery = true)
    List<OrderMenuCategoryEntity> findAllSearchWord(@Param("search") String searchWord);

    @Query(value = "SELECT * FROM menu_categories " +
            "WHERE id IN (:categoryIds) " +
            "OR (LOWER(name) LIKE LOWER(CONCAT('%', :searchWord, '%')) " +
            "OR LOWER(category_description) LIKE LOWER(CONCAT('%', :searchWord, '%'))) " +
            "AND status = 1", nativeQuery = true)
    List<OrderMenuCategoryEntity> getAllByCategoryIdInOrSearchWord(@Param("categoryIds") List<Long> categoryIds,
                                                                    @Param("searchWord") String searchWord);

    OrderMenuCategoryEntity findByIdAndNameAndStatus(Long categoryId, String name, int status);

    OrderMenuCategoryEntity findByIdAndName(Long categoryId, String name);

    List<OrderMenuCategoryEntity> findAllByCategoryKeyInAndStatus(List<String> categoryKeys, int status);

    OrderMenuCategoryEntity findByIdAndStatus(Long categoryId, int status);
}

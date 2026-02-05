package com.coffee.repository;

import com.coffee.entity.OrderMenuItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuItemRepository extends JpaRepository<OrderMenuItemEntity, Long> {

    Page<OrderMenuItemEntity> findAllByStatus(int status, Pageable pageable);

    List<OrderMenuItemEntity> findAllByStatus(int status);

    @Query(value = "SELECT * FROM menu_items " +
            "WHERE (LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(short_description) LIKE LOWER(CONCAT('%', :search, '%'))" +
            "OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND status = 1", nativeQuery = true)
    List<OrderMenuItemEntity> findAllSearchWord(@Param("search") String searchWord);

    OrderMenuItemEntity findByName(String name);

    OrderMenuItemEntity findByNameAndStatus(String name, int status);

    Page<OrderMenuItemEntity> findAllByCategoryIdInAndStatus(List<Long> categoryIds, int status, Pageable pageable);

    OrderMenuItemEntity findByMenuItemKeyAndStatus(String menuItemKey, int status);

    List<OrderMenuItemEntity> findAllByIsFeaturedAndStatus(int featured, int status);

    List<OrderMenuItemEntity> findAllByNameAndStatus(String name, int status);

    List<OrderMenuItemEntity> findByMenuItemKeyInAndStatus(List<String> menuItemKeys, int status);
}

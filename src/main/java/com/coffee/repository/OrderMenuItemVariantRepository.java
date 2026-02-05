package com.coffee.repository;

import com.coffee.entity.OrderMenuItemVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuItemVariantRepository extends JpaRepository<OrderMenuItemVariantEntity, Long> {

    List<OrderMenuItemVariantEntity> findAllByMenuItemIdInAndStatus(List<Long> menuItemIds, int status);

    OrderMenuItemVariantEntity findByMenuItemIdAndStatus(Long id, int status);

    List<OrderMenuItemVariantEntity> findAllByMenuItemIdAndStatus(Long id, int status);

    List<OrderMenuItemVariantEntity> findAllByVariantKeyInAndStatus(List<String> variantKeys, int status);

    List<OrderMenuItemVariantEntity> findAllByIdInAndStatus(List<Long> variantIds, int status);
}

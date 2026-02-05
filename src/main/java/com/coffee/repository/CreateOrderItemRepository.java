package com.coffee.repository;

import com.coffee.entity.CreateOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreateOrderItemRepository extends JpaRepository<CreateOrderItemEntity, Long> {

    List<CreateOrderItemEntity> findAllByOrderIdAndStatus(String orderId, int status);

    List<CreateOrderItemEntity> findAllByOrderIdInAndStatus(List<String> orderIds, int status);
}

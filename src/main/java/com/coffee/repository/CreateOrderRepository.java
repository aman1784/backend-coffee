package com.coffee.repository;

import com.coffee.entity.CreateOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface CreateOrderRepository extends JpaRepository<CreateOrderEntity, Long> {

    CreateOrderEntity findByOrderIdAndOrderKeyAndStatus(String orderId, String orderKey, int status);

    Page<CreateOrderEntity> findByUserIdAndCustomerNameAndCustomerPhoneNumberAndStatus(String userId, String customerName, String customerPhoneNumber, int status, Pageable pageable);

    Page<CreateOrderEntity> findByUserIdAndStatus(String userId, int status, Pageable pageable);

    Optional<CreateOrderEntity> findByIdAndStatus(Long id, int status);

    Page<CreateOrderEntity> findByUserIdAndStatusAndCreatedAtBetween(String userId, int status, OffsetDateTime startDate, OffsetDateTime endDate, Pageable pageable);
}

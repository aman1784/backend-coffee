package com.coffee.repository;

import com.coffee.entity.PaymentLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentLinkRepository extends JpaRepository<PaymentLinkEntity, Long> {

    PaymentLinkEntity findByReferenceIdAndStatus(String referenceId, int status);

    Optional<PaymentLinkEntity> findByIdAndStatus(Long id, int status);
}

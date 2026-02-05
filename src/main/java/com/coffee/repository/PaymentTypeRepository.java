package com.coffee.repository;

import com.coffee.projection.GetPaymentTypeMethodProjection;
import com.coffee.razorpay.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, Long> {

    List<GetPaymentTypeMethodProjection> findAllByStatus(int status);
}

package com.coffee.service.impl;

import com.coffee.entityMapper.PaymentTypeEntityMapper;
import com.coffee.projection.GetPaymentTypeMethodProjection;
import com.coffee.repository.PaymentTypeRepository;
import com.coffee.service.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private final PaymentTypeRepository repository;
    private final PaymentTypeEntityMapper mapper;


    @Override
    public List<GetPaymentTypeMethodProjection> getPaymentMethods() {

        List<GetPaymentTypeMethodProjection> paymentTypes = repository.findAllByStatus(1);
        return paymentTypes.isEmpty() ? Collections.emptyList() : paymentTypes;
    }
}

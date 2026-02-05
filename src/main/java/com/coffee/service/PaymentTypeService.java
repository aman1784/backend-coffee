package com.coffee.service;

import com.coffee.dto.PaymentTypeDto;
import com.coffee.projection.GetPaymentTypeMethodProjection;

import java.util.List;

public interface PaymentTypeService {

    List<GetPaymentTypeMethodProjection> getPaymentMethods();
}

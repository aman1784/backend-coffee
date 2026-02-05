package com.coffee.service;

import com.coffee.dto.CreateOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreateOrderService {

    CreateOrderDto save(CreateOrderDto createOrderDto);

    CreateOrderDto findByOrderIdAndOrderKey(String orderId, String orderKey);

    Page<CreateOrderDto> findByUserIdAndCustomerNameAndCustomerPhoneNumber(String userId, String customerName, String customerPhoneNumber, Pageable pageable);

    Page<CreateOrderDto> findByUserId(String userId, Pageable pageable, String startDate, String endDate);

    CreateOrderDto update(CreateOrderDto createOrderDto);
}

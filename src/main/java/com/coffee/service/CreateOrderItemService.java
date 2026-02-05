package com.coffee.service;

import com.coffee.dto.CreateOrderItemDto;

import java.util.List;

public interface CreateOrderItemService{

    List<CreateOrderItemDto> saveAll(List<CreateOrderItemDto> createOrderItemDtoList);

    List<CreateOrderItemDto> findAllByOrderId(String orderId);

    List<CreateOrderItemDto> findAllByOrderIdIn(List<String> orderIds);
}

package com.coffee.service;

import com.coffee.dto.OrderItemRatingDto;

import java.util.List;

/**
 * @author Aman Kumar Seth
 */

public interface OrderItemRatingService {

    List<OrderItemRatingDto> saveAll(List<OrderItemRatingDto> orderItemRatingDto);

    List<OrderItemRatingDto> findAllByOrderId(String orderId);

    List<OrderItemRatingDto> findAllByMenuItemKeyIn(List<String> menuItemKeys);

    List<OrderItemRatingDto> findAllByMenuItemKey(String menuItemKey);
}

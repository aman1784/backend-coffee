package com.coffee.usecase.impl;

import com.coffee.dto.OrderItemRatingDto;
import com.coffee.exception.GenericException;
import com.coffee.request.RateOrderItem;
import com.coffee.request.RateOrderItemRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.RateOrderItemResponse;
import com.coffee.responseMapper.OrderRatingResponseMapper;
import com.coffee.service.OrderItemRatingService;
import com.coffee.usecase.OrderRatingUseCaseService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aman Kumar Seth
 */

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderRatingUseCaseServiceImpl implements OrderRatingUseCaseService {

    private final OrderRatingResponseMapper orderRatingResponseMapper;

    private final OrderItemRatingService orderItemRatingService;

    @Override
    public BaseResponse<RateOrderItemResponse> rateOrderItem(RateOrderItemRequest request) {
        if (request.getOrderId() == null || request.getOrderId().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Order id is required");
        }
        if (request.getUserName() == null || request.getUserName().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Full User name is required");
        }
        validateOrderItems(request.getOrderItems());
        List<OrderItemRatingDto> orderItemRatingDtoList = orderRatingResponseMapper.mapToOrderItemRatingDto(request);
        log.debug("[OrderRatingUseCaseService][rateOrderItem] orderItemRatingDtoList: {}", orderItemRatingDtoList);
        orderItemRatingDtoList = orderItemRatingService.saveAll(orderItemRatingDtoList);
        log.debug("[OrderRatingUseCaseService][rateOrderItem][saved] orderItemRatingDtoList: {}", orderItemRatingDtoList);
        RateOrderItemResponse response = orderRatingResponseMapper.mapToRateOrderItemResponse(orderItemRatingDtoList);
        return BaseResponse.<RateOrderItemResponse>builder().data(response).build();
    }

    private void validateOrderItems(List<RateOrderItem> orderItems) {
        for (RateOrderItem item : orderItems) {
            if (item.getMenuItemKey() == null || item.getMenuItemKey().isEmpty()) {
                throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Menu item key is required");
            }
        }
    }
}

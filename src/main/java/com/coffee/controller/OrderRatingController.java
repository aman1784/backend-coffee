package com.coffee.controller;

import com.coffee.request.RateOrderItemRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.RateOrderItemResponse;
import com.coffee.usecase.OrderRatingUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aman Kumar Seth
 */

@RestController
@RequestMapping(path = "/api/v1.0/order-rating")
@RequiredArgsConstructor
@Slf4j
public class OrderRatingController {

    private final OrderRatingUseCaseService service;

    /**
     * API to rate order item
     * @param request orderId, orderItems, rating, comments(optional)
     * @return
     */
    @PostMapping(value = "/rateOrderItem")
    public ResponseEntity<BaseResponse<RateOrderItemResponse>> rateOrderItem(@RequestBody RateOrderItemRequest request) {
        log.debug("[OrderRatingController][rateOrderItem] request: {}", request);
        BaseResponse<RateOrderItemResponse> response = service.rateOrderItem(request);
        log.debug("[OrderRatingController][rateOrderItem] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

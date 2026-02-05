package com.coffee.controller;

import com.coffee.request.CreateOrderRequest;
import com.coffee.request.GetAllOrdersRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.CreateOrderResponse;
import com.coffee.response.GetAllOrdersResponse;
import com.coffee.response.GetOrderResponse;
import com.coffee.usecase.CreateOrderUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aman Kumar Seth
 */
@RestController
@RequestMapping(value = "/api/v1.0/orders")
@RequiredArgsConstructor
@Slf4j
public class CreateOrderController {

    private final CreateOrderUseCaseService createOrderUsecaseService;

    /**
     * create order
     * @param request body
     * @return response
     */
    @PostMapping(value = "/createOrder")
    public ResponseEntity<BaseResponse<CreateOrderResponse>> createOrder(@RequestBody CreateOrderRequest request) {
        log.debug("[CreateOrderController][createOrder] request: {}", request);
        BaseResponse<CreateOrderResponse> response = createOrderUsecaseService.createOrder(request);
        log.debug("[CreateOrderController][createOrder] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * get order
     * @param orderId -> mandatory parameter
     * @param orderKey -> mandatory parameter
     * @return response
     */
    @GetMapping(value = "/getOrder/{orderId}/{orderKey}")
    public ResponseEntity<BaseResponse<GetOrderResponse>> getOrder(@PathVariable String orderId, @PathVariable String orderKey) {
        log.debug("[CreateOrderController][getOrder] request: orderId: {}, orderKey: {}", orderId, orderKey);
        BaseResponse<GetOrderResponse> response = createOrderUsecaseService.getOrder(orderId, orderKey);
        log.debug("[CreateOrderController][getOrder] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * get all orders
     * @param page -> default value is 0
     * @param size -> default value is 20
     * @param sort -> default value is id
     * @param userId -> mandatory parameter
     * @param startDate -> optional
     * @param endDate -> optional
     * @return response
     */
    @GetMapping(value = "/getAllOrders")
    public ResponseEntity<BaseResponse<GetAllOrdersResponse>> getAllOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                           @RequestParam(value = "size", defaultValue = "20") int size,
                                                                           @RequestParam(value = "sort", defaultValue = "id") String sort,
                                                                           @RequestParam(value = "userId") String userId,
                                                                           @RequestParam(value = "startDate", required = false) String startDate,
                                                                           @RequestParam(value = "endDate", required = false) String endDate) {
        log.debug("[CreateOrderController][getAllOrders] request page: {}, size: {}, sort: {}, userId: {}, fromDate: {}, toDate: {}", page, size, sort, userId, startDate, endDate);
        BaseResponse<GetAllOrdersResponse> response = createOrderUsecaseService.getAllOrders(page, size, sort, userId, startDate, endDate);
        log.debug("[CreateOrderController][getAllOrders] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

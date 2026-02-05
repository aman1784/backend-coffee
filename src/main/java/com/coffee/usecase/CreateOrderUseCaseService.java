package com.coffee.usecase;

import com.coffee.request.CreateOrderRequest;
import com.coffee.request.GetAllOrdersRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.CreateOrderResponse;
import com.coffee.response.GetAllOrdersResponse;
import com.coffee.response.GetOrderResponse;

public interface CreateOrderUseCaseService {

    BaseResponse<CreateOrderResponse> createOrder(CreateOrderRequest request);

    BaseResponse<GetOrderResponse> getOrder(String orderId, String orderKey);


    BaseResponse<GetAllOrdersResponse> getAllOrders(int page, int size, String sort, String userId, String startDate, String endDate);
}

package com.coffee.usecase;

import com.coffee.request.RateOrderItemRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.RateOrderItemResponse;

/**
 * @author Aman Kumar Seth
 */

public interface OrderRatingUseCaseService {

    BaseResponse<RateOrderItemResponse> rateOrderItem(RateOrderItemRequest request);
}

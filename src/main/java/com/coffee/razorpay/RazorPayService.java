package com.coffee.razorpay;

import com.coffee.response.CreateOrderResponse;

public interface RazorPayService {

    String createPaymentLink(CreateOrderResponse response);
}

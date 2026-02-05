package com.coffee.razorpay;

import com.coffee.response.CreateOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RazorPayServiceImpl implements RazorPayService{

    @Override
    public String createPaymentLink(CreateOrderResponse response) {
        return "";
    }
}

package com.coffee.usecase;

import com.coffee.request.CreatePaymentLinkRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.CreatePaymentLinkResponse;
import com.coffee.response.GetPaymentMethodsResponse;

import java.util.List;

public interface PaymentUseCaseService {

    BaseResponse<List<GetPaymentMethodsResponse>> getPaymentMethods();

    BaseResponse<CreatePaymentLinkResponse> createPaymentLink(CreatePaymentLinkRequest request);
}

package com.coffee.usecase.impl;

import com.coffee.dto.PaymentLinkDto;
import com.coffee.exception.GenericException;
import com.coffee.projection.GetPaymentTypeMethodProjection;
import com.coffee.request.CreatePaymentLinkRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.CreatePaymentLinkResponse;
import com.coffee.response.GetPaymentMethodsResponse;
import com.coffee.responseMapper.PaymentResponseMapper;
import com.coffee.service.PaymentLinkService;
import com.coffee.service.PaymentTypeService;
import com.coffee.usecase.PaymentUseCaseService;
import com.coffee.util.ExceptionConstant;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentUseCaseServiceImpl implements PaymentUseCaseService {

    @Value("${razorpay.key}")
    private String razorPayKey;

    @Value("${razorpay.secret}")
    private String razorPaySecret;

    private final PaymentResponseMapper paymentResponseMapper;

    private final PaymentTypeService paymentTypeService;
    private final PaymentLinkService paymentLinkService;

    @Override
    public BaseResponse<List<GetPaymentMethodsResponse>> getPaymentMethods() {
        List<GetPaymentTypeMethodProjection> paymentTypeList = paymentTypeService.getPaymentMethods();
        log.info("[PaymentUseCaseService][getPaymentMethods] paymentTypeList: {}", paymentTypeList);
        List<GetPaymentMethodsResponse> response = paymentResponseMapper.toGetPaymentMethodsResponse(paymentTypeList);
        return BaseResponse.<List<GetPaymentMethodsResponse>>builder().data(response).build();
    }

    @Override
    public BaseResponse<CreatePaymentLinkResponse> createPaymentLink(CreatePaymentLinkRequest request) {
        validateCreatePaymentLinkRequest(request);
        CreatePaymentLinkResponse response;
        try {
            RazorpayClient razorpay = new RazorpayClient(razorPayKey, razorPaySecret);
            JSONObject paymentLinkRequest = paymentResponseMapper.createPaymentLinkRequest(request.getGrandTotal(), request.getOrderId(), request.getUserId(), request.getCustomerName(), request.getCustomerPhoneNumber(), request.getOrderKey());
            log.debug("[PaymentUseCaseService][createPaymentLink] paymentLinkRequest: {}", paymentLinkRequest);
            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
            log.debug("[PaymentUseCaseService][createPaymentLink] payment: {}", payment);
            response = paymentResponseMapper.createPaymentLinkResponse(payment);
            log.debug("[PaymentUseCaseService][createPaymentLink] response: {}", response);
            PaymentLinkDto paymentLink = paymentResponseMapper.createPaymentLinkDto(payment, request);
            log.debug("[PaymentUseCaseService][createPaymentLink] paymentLink: {}", paymentLink);
            paymentLink = paymentLinkService.save(paymentLink);
            log.debug("[PaymentUseCaseService][createPaymentLink] paymentLink created successfully: {}", paymentLink);
            log.info("[PaymentUseCaseService][createPaymentLink] Payment link created successfully: {}", response);
        } catch (Exception e) {
            log.debug("[PaymentUseCaseService][createPaymentLink] Error while creating payment link", e);
            log.error("[PaymentUseCaseService][createPaymentLink] Error while creating payment link", e);
            log.error("Razorpay API error: ", e);
            throw new GenericException(ExceptionConstant.requestFailedCode, ExceptionConstant.requestFailedMessage, "Unable to process request", e.getMessage());
        }
        return BaseResponse.<CreatePaymentLinkResponse>builder().data(response).build();
    }

    private void validateCreatePaymentLinkRequest(CreatePaymentLinkRequest request) {
        if (request.getPaymentType() == null || request.getPaymentType().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Payment type can not be null or empty");
        }
        if (request.getOrderId() == null || request.getOrderId().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Order id can not be null or empty");
        }
        if (request.getGrandTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Grand total can not be zero or negative");
        }
        if (request.getGrandTotal().compareTo(BigDecimal.valueOf(500000)) > 0) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.requestFailedMessage, "Amount cannot more than 5 lakh");
        }
    }
}

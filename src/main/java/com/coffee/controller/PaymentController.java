package com.coffee.controller;

import com.coffee.request.CreatePaymentLinkRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.CreatePaymentLinkResponse;
import com.coffee.response.GetPaymentMethodsResponse;
import com.coffee.usecase.PaymentUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1.0/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentUseCaseService service;

    @GetMapping(value = "/getPaymentMethods")
    public ResponseEntity<BaseResponse<List<GetPaymentMethodsResponse>>> getPaymentMethods() {
        log.debug("[PaymentController][getPaymentMethods] request");
        BaseResponse<List<GetPaymentMethodsResponse>> response = service.getPaymentMethods();
        log.debug("[PaymentController][getPaymentMethods] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/create/paymentLink")
    public ResponseEntity<BaseResponse<CreatePaymentLinkResponse>> createPaymentLink(@RequestBody CreatePaymentLinkRequest request){
        log.debug("[PaymentController][createPaymentLink] request : {}", request);
        BaseResponse<CreatePaymentLinkResponse> response = service.createPaymentLink(request);
        log.debug("[PaymentController][createPaymentLink] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

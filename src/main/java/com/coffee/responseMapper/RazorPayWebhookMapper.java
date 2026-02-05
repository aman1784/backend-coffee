package com.coffee.responseMapper;

import com.coffee.dto.CreateOrderDto;
import com.coffee.dto.PaymentLinkDto;
import com.coffee.enums.OrderStatus;
import com.coffee.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RazorPayWebhookMapper {

    public PaymentLinkDto buildPaymentLinkDto(PaymentLinkDto paymentLinkDto, String event, String errorReason, String errorDescription,
                                              String errorSource, String errorStep, String status) {

        paymentLinkDto.setAmountPaid(BigDecimal.ZERO);
        paymentLinkDto.setPaymentStatus(status); // "failed"
        paymentLinkDto.setEventType(event);
        paymentLinkDto.setErrorReason(errorReason);
        paymentLinkDto.setErrorDescription(errorDescription);
        paymentLinkDto.setErrorSource(errorSource);
        paymentLinkDto.setErrorStep(errorStep);
        return paymentLinkDto;
    }

    public PaymentLinkDto buildPaymentLinkDtoForPaymentLinkPaid(PaymentLinkDto paymentLinkDto, String event,
                                                                String status, BigDecimal amountInRupees) {

        paymentLinkDto.setAmountPaid(amountInRupees);
        paymentLinkDto.setPaymentStatus(PaymentStatus.PAID.getValue());
        paymentLinkDto.setEventType(event);
        return paymentLinkDto;
    }

    public CreateOrderDto buildUpdateCreateOrderDtoForPaymentLinkPaid(CreateOrderDto createOrderDto,
                                                                      PaymentLinkDto paymentLinkDto) {
        createOrderDto.setOrderStatus(OrderStatus.ORDER_CONFIRMED.getValue());
        createOrderDto.setPaymentStatus(PaymentStatus.PAID.getValue());
        return createOrderDto;
    }
}

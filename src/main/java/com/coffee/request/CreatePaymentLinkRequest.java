package com.coffee.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentLinkRequest extends BaseRequest{

    private String userId;
    private String cartId;
    private String orderId;
    private String paymentType;
    private BigDecimal grandTotal;
    private String customerName;
    private String customerPhoneNumber;
    private String orderKey;
}

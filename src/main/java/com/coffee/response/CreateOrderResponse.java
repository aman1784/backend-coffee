package com.coffee.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderResponse {

    private String orderId;
    private String orderKey;
    private String customerName;
    private String customerPhoneNumber;
    private BigDecimal total;

    private OffsetDateTime createdAt;
    private String userId;
    private String orderMessage;
    private String paymentLink;
}

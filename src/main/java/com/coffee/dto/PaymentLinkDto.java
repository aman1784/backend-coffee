package com.coffee.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentLinkDto {

    private Long id;
    private String paymentId;
    private String userKey;
    private String notes;
    private String description;
    private String referenceId;
    private String url;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private String currency;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private int status;
    private String paymentStatus;
    private String orderId;
    private String orderKey;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String eventType;
    private String errorReason;
    private String errorDescription;
    private String errorSource;
    private String errorStep;
}

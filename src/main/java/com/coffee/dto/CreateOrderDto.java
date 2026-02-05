package com.coffee.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderDto {

    private Long id;
    private String orderId;
    private String orderKey;
    private String customerName;
    private String customerPhoneNumber;
    private BigDecimal subTotal;
    private BigDecimal gst;
    private BigDecimal discount;
    private BigDecimal total;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String userId;
    private String orderStatus;
    private String paymentLink;
    private String paymentStatus;
    private String paymentType; // Cash or Online
}

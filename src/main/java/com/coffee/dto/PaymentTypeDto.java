package com.coffee.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PaymentTypeDto {

    private Long id;
    private String paymentType;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

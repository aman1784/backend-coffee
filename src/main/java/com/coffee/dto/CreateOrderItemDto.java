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
public class CreateOrderItemDto {

    private Long id;
    private String orderId;
    private String itemName;
    private BigDecimal itemQuantity;
    private BigDecimal itemPrice;
    private BigDecimal itemDiscount;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long itemId;
    private BigDecimal itemTotal;
    private Long variantId;
    private String variantName;
    private String menuItemKey;
}

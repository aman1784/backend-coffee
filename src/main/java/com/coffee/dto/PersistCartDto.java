package com.coffee.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PersistCartDto {

    private Long id;
    private String userId;
    private Long itemId;
    private String itemName;
    private BigDecimal itemQuantity;
    private BigDecimal itemPrice;
    private BigDecimal itemDiscount;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long variantId;
    private String variantName;
    private String menuItemKey;
}

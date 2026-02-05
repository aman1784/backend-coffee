package com.coffee.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartOrderItemRequest {

    private Long itemId;
    private String itemName;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal discount;
    private Long variantId;
    private String variantName;
    private String menuItemKey;
}

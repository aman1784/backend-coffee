package com.coffee.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Aman Kumar Seth
 * @since 27-01-2026
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareCartDetailItemResponse {

    private Long itemId;
    private String itemName;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal discount;
    private Long variantId;
    private String variantName;
    private String menuItemKey;

}

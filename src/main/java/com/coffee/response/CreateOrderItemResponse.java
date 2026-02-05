package com.coffee.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderItemResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String orderItemName;
    private BigDecimal orderItemPrice;
    private BigDecimal orderItemQuantity;
    private BigDecimal orderItemDiscount;
    private BigDecimal orderItemTotalPrice;
    private String menuItemKey; // To get the rating: this is the menu_item_key from the menu_items table
}

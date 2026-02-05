package com.coffee.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderItemRequest {

    @NotNull(message = "Item name can not be null")
    @NotBlank(message = "Item name can not be blank")
    private String itemName;

    @NotNull(message = "Item quantity can not be null")
    @NotBlank(message = "Item quantity can not be blank")
    @NotEmpty(message = "Item quantity can not be empty")
    private BigDecimal itemQuantity;

    private BigDecimal itemPrice;
    private BigDecimal itemDiscount;
    private Long itemId;

    private Long variantId;
    private String variantName;

    private String menuItemKey; // this is the key of the menu item for getting the rating of the item
}

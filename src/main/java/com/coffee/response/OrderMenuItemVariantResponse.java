package com.coffee.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMenuItemVariantResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long menuItemId; // Link to menu item
    private String variantName; // Name of the variant
    private BigDecimal sellingPrice; // Price charged to customer
    private BigDecimal mrp; // Maximum Retail Price
    private BigDecimal cost; // Internal cost
    private String sku;
    private String variantKey;
}

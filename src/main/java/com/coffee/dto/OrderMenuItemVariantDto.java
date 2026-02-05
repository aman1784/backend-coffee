package com.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMenuItemVariantDto {

    private Long id; // Unique identifier
    private Long menuItemId; // Link to menu item
    private String variantName; // Name of the variant
    private BigDecimal sellingPrice; // Price charged to customer
    private BigDecimal mrp; // Maximum Retail Price
    private BigDecimal cost; // Internal cost
    private String sku; // SKU code for inventory tracking
    private int status; // Active/inactive status
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String variantKey;
}
package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "menu_item_variants") // Maps this class to the "menu_item_variants" table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMenuItemVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    @Column(name = "menu_item_id", nullable = false) // Foreign key to menu_items.id
    private Long menuItemId;

    @Column(name = "variant_name", length = 50, nullable = false) // Variant name, e.g., "Large", "Medium"
    private String variantName;

    @Column(name = "selling_price", nullable = false, precision = 10, scale = 2) // Price charged to customer
    private BigDecimal sellingPrice;

    @Column(name = "mrp", precision = 10, scale = 2) // Maximum Retail Price (optional)
    private BigDecimal mrp;

    @Column(name = "cost", precision = 10, scale = 2) // Internal cost of production (optional)
    private BigDecimal cost;

    @Column(name = "sku", length = 50) // Stock Keeping Unit for inventory (optional)
    private String sku;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1") // Active/inactive flag (default: active)
    private int status;

    @Column(name = "created_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") // Timestamp of creation
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP") // Last update time
    private OffsetDateTime updatedAt;

    @Column(name = "variant_key", nullable = false, unique = true)
    private String variantKey;
}
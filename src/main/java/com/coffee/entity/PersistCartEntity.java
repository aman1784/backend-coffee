package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "persist_carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersistCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 72)
    private String userId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name", length = 255)
    private String itemName;

    @Column(name = "item_quantity", precision = 9, scale = 2)
    private BigDecimal itemQuantity;

    @Column(name = "item_price", precision = 9, scale = 2)
    private BigDecimal itemPrice;

    @Column(name = "item_discount", precision = 9, scale = 2)
    private BigDecimal itemDiscount;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "menu_item_key")
    private String menuItemKey;

}

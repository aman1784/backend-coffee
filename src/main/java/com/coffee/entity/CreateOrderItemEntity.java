package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, length = 72)
    private String orderId;

    @Column(name = "item_name", nullable = false, length = 72)
    private String itemName;

    @Column(name = "item_quantity", nullable = false, precision = 9, scale = 2)
    private BigDecimal itemQuantity;

    @Column(name = "item_price", nullable = false, precision = 9, scale = 2)
    private BigDecimal itemPrice;

    @Column(name = "item_discount", precision = 9, scale = 2)
    private BigDecimal itemDiscount;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "item_total", nullable = false, precision = 9, scale = 2)
    private BigDecimal itemTotal;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "menu_item_key")
    private String menuItemKey;

}

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
 * @since 27-01-2026
 */

@Entity
@Table(name = "share_cart_link_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareCartLinkItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "share_cart_link_id", nullable = false)
    private Long shareCartLinkId; // ID from ShareCartLinkDto

    @Column(name = "created_by_user_id")
    private String createdByUserId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_quantity")
    private BigDecimal itemQuantity;

    @Column(name = "item_price")
    private BigDecimal itemPrice;

    @Column(name = "item_discount")
    private BigDecimal itemDiscount;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "menu_item_key")
    private String menuItemKey;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}

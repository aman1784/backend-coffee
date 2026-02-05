package com.coffee.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 * @since 27-01-2026
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ShareCartLinkItemDto {

    private Long id;
    private Long shareCartLinkId; // ID from ShareCartLinkDto
    private String createdByUserId;
    private Long itemId;
    private String itemName;
    private BigDecimal itemQuantity;
    private BigDecimal itemPrice;
    private BigDecimal itemDiscount;
    private Long variantId;
    private String variantName;
    private String menuItemKey;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}

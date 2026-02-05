package com.coffee.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
//public class OrderMenuItemResponse {
//
//    private Long menuItemId;
//    private String menuItemKey;
//    private String menuItemName;
//    private String menuItemSlug;
//    private String menuItemShortDescription;
//    private String menuItemDescription;
//    private String menuItemIngredients;
//    private String menuItemNutritionInfo;
//    private String menuItemImage;
//
//    private BigDecimal menuItemSellingPrice;
//    private BigDecimal menuItemMrp;
//
//    private String menuItemVariantName;
//    private String menuItemSku;
//
//    private Long menuItemCategoryId;
//    private String menuItemCategoryKey;
//    private String menuItemCategoryName;
//    private String menuItemCategorySlug;
//    private String menuItemCategoryDescription;
//    private String menuItemCategoryMetaDescription;
//
//    // ============
//
//    // ✅ NEW: List of variants
//    private List<OrderMenuPriceDto> priceVariants;
//
//}

@Builder
public class OrderMenuItemResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long menuItemId;
    private String menuItemKey;
    private String menuItemName;
    private String menuItemSlug;
    private String menuItemShortDescription;
    private String menuItemDescription;
    private String menuItemIngredients;
    private String menuItemNutritionInfo;
    private List<String> menuItemImage;
    private int isFeatured;

    private Long menuItemCategoryId;
    private String menuItemCategoryKey;
    private String menuItemCategoryName;
    private String menuItemCategorySlug;
    private String menuItemCategoryDescription;
    private String menuItemCategoryMetaDescription;

    // ✅ NEW: List of variants
    private List<OrderMenuItemVariantResponse> priceVariants;

    private List<Map<String, Object>> rating;
    private List<Map<String, Object>> review;
}

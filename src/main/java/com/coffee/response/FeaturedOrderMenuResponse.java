package com.coffee.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeaturedOrderMenuResponse implements Serializable {

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
    private String menuItemImage;

    private Long menuItemCategoryId;
    private String menuItemCategoryKey;
    private String menuItemCategoryName;
    private String menuItemCategorySlug;
    private String menuItemCategoryDescription;
    private String menuItemCategoryMetaDescription;

    // ✅ NEW: List of variants
    private List<OrderMenuItemVariantResponse> priceVariants;
}

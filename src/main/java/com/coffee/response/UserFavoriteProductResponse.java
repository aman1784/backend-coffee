package com.coffee.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserFavoriteProductResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Map<String, Object>> userFavoriteProducts;
//
//    private String menuItemId;
//    private String menuItemKey;
//    private String menuItemName;
//    private String menuItemSlug;
//    private String menuItemShortDescription;
//    private String menuItemDescription;
//    private String menuItemIngredients;
//    private String menuItemNutritionInfo;
//    private String menuItemImage;
//    private String menuItemCategoryId;
//    private List<Map<String, Object>> priceVariants;
//    private List<Map<String, Object>> rating;
//    private List<Map<String, Object>> review;

}

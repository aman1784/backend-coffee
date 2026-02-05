package com.coffee.responseMapper;

import com.coffee.dto.OrderMenuItemDto;
import com.coffee.dto.OrderMenuItemVariantDto;
import com.coffee.dto.UserFavoriteProductDto;
import com.coffee.request.UserFavoriteProductRequest;
import com.coffee.response.UserFavoriteProductResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Lazy
public class UserFavoriteProductResponseMapper {

    public UserFavoriteProductResponse buildUserFavoriteProductResponse(
            String userId,
            List<UserFavoriteProductDto> userFavoriteProductDtoList,
            List<OrderMenuItemDto> orderMenuItemDtoList,
            List<String> menuItemKeys,
            List<OrderMenuItemVariantDto> orderMenuItemVariantDtoList,
            List<String> variantKeys) {

        List<Map<String, Object>> favoriteProducts = new ArrayList<>();

        for (UserFavoriteProductDto favorite : userFavoriteProductDtoList) {
            Map<String, Object> productMap = new HashMap<>();

            // Find matching menu item
            OrderMenuItemDto menuItem = orderMenuItemDtoList.stream()
                    .filter(item -> item.getMenuItemKey().equals(favorite.getMenuItemKey()))
                    .findFirst()
                    .orElse(null);

            if (menuItem != null) {
                productMap.put("menuItemId", menuItem.getId());
                productMap.put("menuItemKey", menuItem.getMenuItemKey());
                productMap.put("menuItemName", menuItem.getName());
                productMap.put("menuItemSlug", menuItem.getSlug());
                productMap.put("menuItemShortDescription", menuItem.getShortDescription());
                productMap.put("menuItemDescription", menuItem.getDescription());
                productMap.put("menuItemIngredients", menuItem.getIngredients());
                productMap.put("menuItemNutritionInfo", menuItem.getNutritionInfo());
                productMap.put("menuItemImage", mapImageResponse(menuItem.getImage()));
                productMap.put("menuItemCategoryId", menuItem.getCategoryId());
            }

            // Find matching variant(s)
            List<Map<String, Object>> variantMaps = orderMenuItemVariantDtoList.stream()
                    .filter(variant -> variant.getVariantKey().equals(favorite.getVariantKey()))
                    .map(variant -> {
                        Map<String, Object> variantMap = new HashMap<>();
                        variantMap.put("variantId", variant.getId());
                        variantMap.put("variantName", variant.getVariantName());
                        variantMap.put("sellingPrice", variant.getSellingPrice());
                        variantMap.put("mrp", variant.getMrp());
                        variantMap.put("cost", variant.getCost());
                        variantMap.put("sku", variant.getSku());
                        variantMap.put("variantKey", variant.getVariantKey());
                        return variantMap;
                    })
                    .collect(Collectors.toList());

            productMap.put("priceVariants", variantMaps);

            // Ratings & Reviews can be populated later
            productMap.put("rating", new ArrayList<Map<String, Object>>());
            productMap.put("review", new ArrayList<Map<String, Object>>());

            favoriteProducts.add(productMap);
        }

        return UserFavoriteProductResponse.builder()
                .userFavoriteProducts(favoriteProducts)
                .build();
    }

    private List<String> mapImageResponse(String image) {
        if (image == null || image.isEmpty()) {
            return Collections.emptyList();
        }
        String[] imageUrls = image.split(", ");
        return Arrays.asList(imageUrls);
    }

    public UserFavoriteProductDto mapToUserFavoriteProductDto(UserFavoriteProductRequest request) {

        return UserFavoriteProductDto.builder()
                .id(null)
                .userId(request.getUserId())
                .menuItemKey(request.getMenuItemKey())
                .variantKey(request.getVariantKey())
                .status(1)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}

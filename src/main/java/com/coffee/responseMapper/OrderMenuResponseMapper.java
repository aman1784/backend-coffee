package com.coffee.responseMapper;

import com.coffee.dto.OrderItemRatingDto;
import com.coffee.dto.OrderMenuCategoryDto;
import com.coffee.dto.OrderMenuItemDto;
import com.coffee.dto.OrderMenuItemVariantDto;
import com.coffee.exception.GenericException;
import com.coffee.request.AddItemInMenuAndCategoryRequest;
import com.coffee.request.AddOrderMenuCategory;
import com.coffee.response.*;
import com.coffee.util.ExceptionConstant;
import com.coffee.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@Lazy
public class OrderMenuResponseMapper {

    public OrderMenuItemPageResponse buildOrderMenuPageResponse(Page<OrderMenuItemDto> orderMenuItemDtos,
                                                                List<OrderMenuCategoryDto> orderMenuCategoryDtos,
                                                                List<OrderMenuItemVariantDto> OrderMenuItemVariantDtos,
                                                                List<OrderItemRatingDto> orderItemRatingDtos) {

        // Map categoryId -> OrderMenuCategoryDto for quick lookup
        Map<Long, OrderMenuCategoryDto> categoryMap = orderMenuCategoryDtos.stream()
                                                            .filter(Objects::nonNull)
                                                            .filter(dto -> dto.getId() != null)
                                                            .collect(Collectors.toMap(OrderMenuCategoryDto::getId, dto -> dto));

        // ✅ Group menuItemId -> List<OrderMenuItemVariantDto>
        Map<Long, List<OrderMenuItemVariantDto>> priceMap = OrderMenuItemVariantDtos.stream()
                .filter(Objects::nonNull)
                .filter(dto -> dto.getMenuItemId() != null)
                .collect(Collectors.groupingBy(OrderMenuItemVariantDto::getMenuItemId));

        // ✅ Group menuItemRating -> List<OrderItemRatingDto>
        Map<String, List<OrderItemRatingDto>> ratingMap = new HashMap<>();
        if (orderItemRatingDtos != null && !orderItemRatingDtos.isEmpty()) {
            ratingMap = orderItemRatingDtos.stream()
                    .filter(Objects::nonNull)
                    .filter(dto -> dto.getMenuItemKey() != null)
                    .collect(Collectors.groupingBy(OrderItemRatingDto::getMenuItemKey));
        }
        // Convert OrderMenuItemDto to OrderMenuItemResponse
        Map<String, List<OrderItemRatingDto>> finalRatingMap = ratingMap;

        List<OrderMenuItemResponse> menuResponses = orderMenuItemDtos.getContent().stream()
                .filter(Objects::nonNull)
                .map(menuDto -> {
                    OrderMenuCategoryDto categoryDto = categoryMap.get(menuDto.getCategoryId());
                    List<OrderMenuItemVariantDto> priceVariants = priceMap.getOrDefault(menuDto.getId(), Collections.emptyList());
                    List<OrderItemRatingDto> orderItemRatingDtoList = finalRatingMap.getOrDefault(menuDto.getMenuItemKey(), Collections.emptyList());

                    return OrderMenuItemResponse.builder()
                            .menuItemId(menuDto.getId())
                            .menuItemKey(menuDto.getMenuItemKey())
                            .menuItemName(menuDto.getName())
                            .menuItemSlug(menuDto.getSlug())
                            .menuItemShortDescription(menuDto.getShortDescription())
                            .menuItemDescription(menuDto.getDescription())
                            .menuItemIngredients(menuDto.getIngredients())
                            .menuItemNutritionInfo(menuDto.getNutritionInfo())
                            .menuItemImage(mapImageList(menuDto.getImage()))
                            .isFeatured(menuDto.getIsFeatured() == null || menuDto.getIsFeatured() == 0 ? 0 : 1)
                            .menuItemCategoryId(menuDto.getCategoryId())
                            .menuItemCategoryKey(categoryDto != null ? categoryDto.getCategoryKey() : null)
                            .menuItemCategoryName(categoryDto != null ? categoryDto.getName() : null)
                            .menuItemCategorySlug(categoryDto != null ? categoryDto.getSlug() : null)
                            .menuItemCategoryDescription(categoryDto != null ? categoryDto.getCategoryDescription() : null)
                            .menuItemCategoryMetaDescription(categoryDto != null ? categoryDto.getMetaDescription() : null)
                            .priceVariants(buildListOfPriceVariants(priceVariants))
                            .rating(buildRating(orderItemRatingDtoList))
                            .review(buildReview(orderItemRatingDtoList))
                            .build();
                })
                .collect(Collectors.toList());

        return OrderMenuItemPageResponse.builder()
                .menus(menuResponses)
                .currentPage(orderMenuItemDtos.getNumber())
                .totalPages(orderMenuItemDtos.getTotalPages())
                .totalElements(orderMenuItemDtos.getTotalElements())
                .pageSize(orderMenuItemDtos.getSize())
                .build();
    }

    private List<Map<String, Object>> buildRating(List<OrderItemRatingDto> orderItemRatingDtoList) {
        if (orderItemRatingDtoList == null || orderItemRatingDtoList.isEmpty()) {
            return Collections.emptyList();
        }

        return orderItemRatingDtoList.stream()
                .filter(Objects::nonNull)
                .filter(dto -> dto.getMenuItemKey() != null)
                .collect(Collectors.groupingBy(OrderItemRatingDto::getMenuItemKey))
                .entrySet().stream()
                .map(entry -> {
                    String menuItemKey = entry.getKey();
                    List<OrderItemRatingDto> ratings = entry.getValue();

                    int totalRatings = ratings.size();
                    double averageRating = ratings.stream()
                            .filter(Objects::nonNull)
                            .mapToDouble(OrderItemRatingDto::getRating)
                            .average()
                            .orElse(0.0);

                    Map<String, Object> ratingSummary = new HashMap<>();
                    ratingSummary.put("menuItemKey", menuItemKey);
                    ratingSummary.put("totalRatings", totalRatings);
                    ratingSummary.put("averageRating", averageRating);

                    return ratingSummary;
                })
                .collect(Collectors.toList());
    }

    private List<String> mapImageList(String image) {
        if (image == null || image.isEmpty()) {
            return Collections.emptyList();
        }
        String[] imageUrls = image.split(",");
        return Arrays.asList(imageUrls);
    }

    public OrderMenuSearchResponse buildOrderMenuSearchResponse(List<OrderMenuItemDto> orderMenuDtos,
                                                                List<OrderMenuCategoryDto> orderMenuCategoryDtos,
                                                                List<OrderMenuItemVariantDto> OrderMenuItemVariantDtos) {

        // --- Group category by ID ---
        Map<Long, OrderMenuCategoryDto> categoryMap = orderMenuCategoryDtos.stream()
                .filter(Objects::nonNull)
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(OrderMenuCategoryDto::getId, Function.identity()));

        // --- Group prices by menuItemId ---
        Map<Long, List<OrderMenuItemVariantDto>> priceMap;
        if (OrderMenuItemVariantDtos != null) {
            priceMap = OrderMenuItemVariantDtos.stream()
                    .filter(Objects::nonNull)
                    .filter(dto -> dto.getMenuItemId() != null)
                    .collect(Collectors.groupingBy(OrderMenuItemVariantDto::getMenuItemId));
        } else {
            log.warn("[OrderMenuResponseMapper][buildOrderMenuSearchResponse] OrderMenuItemVariantDtos is null");
            priceMap  = new HashMap<>();
        }

        // --- Convert menu items to response ---
        List<OrderMenuItemResponse> menuItemResponses = orderMenuDtos.stream()
                .filter(Objects::nonNull)
                .map(item -> {
                    // Fetch category info
                    OrderMenuCategoryDto category = categoryMap.get(item.getCategoryId());

                    // Fetch and convert price variants
                    List<OrderMenuItemVariantDto> priceDtos = priceMap.getOrDefault(item.getId(), Collections.emptyList());
                    List<OrderMenuItemVariantResponse> priceResponses = priceDtos.stream()
                            .map(price -> OrderMenuItemVariantResponse.builder()
                                    .id(price.getId())
                                    .menuItemId(price.getMenuItemId())
                                    .variantName(price.getVariantName())
                                    .sellingPrice(price.getSellingPrice())
                                    .mrp(price.getMrp())
                                    .cost(price.getCost())
                                    .sku(price.getSku())
                                    .build())
                            .collect(Collectors.toList());

                    return OrderMenuItemResponse.builder()
                            .menuItemId(item.getId())
                            .menuItemKey(item.getMenuItemKey())
                            .menuItemName(item.getName())
                            .menuItemSlug(item.getSlug())
                            .menuItemShortDescription(item.getShortDescription())
                            .menuItemDescription(item.getDescription())
                            .menuItemIngredients(item.getIngredients())
                            .menuItemNutritionInfo(item.getNutritionInfo())
                            .menuItemImage(mapImageList(item.getImage()))
                            .menuItemCategoryId(item.getCategoryId())
                            .menuItemCategoryKey(category != null ? category.getCategoryKey() : null)
                            .menuItemCategoryName(category != null ? category.getName() : null)
                            .menuItemCategorySlug(category != null ? category.getSlug() : null)
                            .menuItemCategoryDescription(category != null ? category.getCategoryDescription() : null)
                            .menuItemCategoryMetaDescription(category != null ? category.getMetaDescription() : null)
                            .priceVariants(priceResponses)
                            .build();
                })
                .collect(Collectors.toList());

        // --- Convert categories to response ---
        List<OrderMenuCategoryResponse> categoryResponses = orderMenuCategoryDtos.stream()
                .filter(Objects::nonNull)
                .map(cat -> OrderMenuCategoryResponse.builder()
                        // .categoryId(cat.getId())
                        .categoryKey(cat.getCategoryKey())
                        .categoryName(cat.getName())
                        .build())
                .collect(Collectors.toList());

        // --- Build final response ---
        if (menuItemResponses.isEmpty() && categoryResponses.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Menu Items Not Found");
        }
        return OrderMenuSearchResponse.builder()
                .menus(menuItemResponses)
                .categories(categoryResponses)
                .build();
    }


//    public OrderMenuItemDto buildOrderMenuDtoFromAddMenuItem(AddItemInMenuAndCategoryRequest request, OrderMenuCategoryDto orderMenuCategoryDto) {
//        AddOrderMenuItem addOrderMenuItem = request.getMenuItem();
//        return OrderMenuItemDto.builder()
//                .menuItemKey(Utility.generateMd5Hash(addOrderMenuItem.getName() + "_" + addOrderMenuItem.getMrp() + "_" + new Date() + "_" + "item"))
//                .name(addOrderMenuItem.getName())
//                .description(addOrderMenuItem.getDescription())
//                .mrp(addOrderMenuItem.getMrp())
//                .image(addOrderMenuItem.getImage())
//                .status(addOrderMenuItem.isEnable() ? 1 : 0)
//                .categoryId(orderMenuCategoryDto.getId())
//                .build();
//    }

    public OrderMenuCategoryDto buildOrderMenuCategoryDtoFromAddMenuCategory(AddItemInMenuAndCategoryRequest request) {
        AddOrderMenuCategory addOrderMenuCategory = request.getMenuCategory();
        return OrderMenuCategoryDto.builder()
                .categoryKey(Utility.generateMd5Hash("Category_1234567890_" + addOrderMenuCategory.getName() + "_" + new Date() + "_" + "category"))
                .name(addOrderMenuCategory.getName())
                .categoryDescription(addOrderMenuCategory.getDescription())
                .image(addOrderMenuCategory.getImage())
                .status(addOrderMenuCategory.isEnable() ? 1 : 0)
                .build();
    }

    public List<OrderMenuCategoryResponse> buildListOfCategoriesResponse(List<OrderMenuCategoryDto> dtos) {
        List<OrderMenuCategoryResponse> responseList = new ArrayList<>();
        for (OrderMenuCategoryDto dto : dtos) {
            OrderMenuCategoryResponse response = OrderMenuCategoryResponse.builder()
                    // .categoryId(dto.getId())
                    .categoryKey(dto.getCategoryKey())
                    .categoryName(dto.getName())
                    .build();

            responseList.add(response);
        }
        return responseList;
    }

    public OrderMenuItemResponse buildOrderMenuItemResponse(OrderMenuItemDto orderMenuItemDto,
                                                            OrderMenuCategoryDto orderMenuCategoryDto,
                                                            List<OrderMenuItemVariantDto> OrderMenuItemVariantDtos,
                                                            List<OrderItemRatingDto> orderItemRatingDtos) {
        return OrderMenuItemResponse.builder()
                .menuItemId(orderMenuItemDto.getId())
                .menuItemKey(orderMenuItemDto.getMenuItemKey())
                .menuItemName(orderMenuItemDto.getName())
                .menuItemSlug(orderMenuItemDto.getSlug())
                .menuItemShortDescription(orderMenuItemDto.getShortDescription())
                .menuItemDescription(orderMenuItemDto.getDescription())
                .menuItemIngredients(orderMenuItemDto.getIngredients())
                .menuItemNutritionInfo(orderMenuItemDto.getNutritionInfo())
                .menuItemImage(mapImageList(orderMenuItemDto.getImage()))
                .isFeatured(orderMenuItemDto.getIsFeatured() == null || orderMenuItemDto.getIsFeatured() == 0 ? 0 : 1)
//                .menuItemSellingPrice(OrderMenuItemVariantDto != null ? OrderMenuItemVariantDto.getSellingPrice() : null)
//                .menuItemMrp(OrderMenuItemVariantDto != null ? OrderMenuItemVariantDto.getMrp() : null)
//                .menuItemVariantName(OrderMenuItemVariantDto != null ? OrderMenuItemVariantDto.getVariantName() : null)
//                .menuItemSku(OrderMenuItemVariantDto != null ? OrderMenuItemVariantDto.getSku() : null)
                .menuItemCategoryId(orderMenuItemDto.getCategoryId())
                .menuItemCategoryKey(orderMenuCategoryDto != null ? orderMenuCategoryDto.getCategoryKey() : null)
                .menuItemCategoryName(orderMenuCategoryDto != null ? orderMenuCategoryDto.getName() : null)
                .menuItemCategorySlug(orderMenuCategoryDto != null ? orderMenuCategoryDto.getSlug() : null)
                .menuItemCategoryDescription(orderMenuCategoryDto != null ? orderMenuCategoryDto.getCategoryDescription() : null)
                .menuItemCategoryMetaDescription(orderMenuCategoryDto != null ? orderMenuCategoryDto.getMetaDescription() : null)
                .priceVariants(buildListOfPriceVariants(OrderMenuItemVariantDtos))
                .rating(buildRating(orderItemRatingDtos))
                .review(buildReview(orderItemRatingDtos))
                .build();
    }

    private List<Map<String, Object>> buildReview(List<OrderItemRatingDto> orderItemRatingDtos) {
        if (orderItemRatingDtos == null || orderItemRatingDtos.isEmpty()) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> responseList = new ArrayList<>();
        for (OrderItemRatingDto dto : orderItemRatingDtos) {
            Map<String, Object> response = new HashMap<>();
            response.put("rating", dto.getRating());
            response.put("comment", dto.getComment());
            response.put("image", null);
            response.put("userName", dto.getUserName());
            responseList.add(response);
        }
        return responseList;
    }

    private List<OrderMenuItemVariantResponse> buildListOfPriceVariants(List<OrderMenuItemVariantDto> OrderMenuItemVariantDtos) {
        List<OrderMenuItemVariantResponse> responseList = new ArrayList<>();
        for (OrderMenuItemVariantDto dto : OrderMenuItemVariantDtos) {
            OrderMenuItemVariantResponse response = OrderMenuItemVariantResponse.builder()
                    .id(dto.getId())
                    .menuItemId(dto.getMenuItemId())
                    .variantName(dto.getVariantName())
                    .sellingPrice(dto.getSellingPrice())
                    .mrp(dto.getMrp())
                    .cost(dto.getCost())
                    .sku(dto.getSku())
                    .variantKey(dto.getVariantKey())
                    .build();

            responseList.add(response);
        }
        return responseList;
    }


    public AddItemInMenuAndCategoryResponse buildAddItemInMenuAndCategoryResponse(OrderMenuCategoryDto orderMenuCategoryDto,
                                                                                  OrderMenuItemDto orderMenuDto) {

        return AddItemInMenuAndCategoryResponse.builder()
                .menuCategory(orderMenuCategoryDto)
                .menuItem(orderMenuDto)
                .build();

    }

    public List<FeaturedOrderMenuResponse> buildFeaturedOrderMenuResponse(List<OrderMenuItemDto> orderMenuItemDtoList,
                                                                    List<OrderMenuItemVariantDto> orderMenuItemVariantDtos) {
        if (orderMenuItemDtoList == null || orderMenuItemDtoList.isEmpty()) {
            return null; // or throw an exception
        }
        // Group variants by menuItemId for fast lookup
        Map<Long, List<OrderMenuItemVariantResponse>> variantsByMenuItemId = orderMenuItemVariantDtos.stream()
                .collect(Collectors.groupingBy(
                        OrderMenuItemVariantDto::getMenuItemId,
                        Collectors.mapping(variant -> OrderMenuItemVariantResponse.builder()
                                        .id(variant.getId())
                                        .menuItemId(variant.getMenuItemId())
                                        .variantName(variant.getVariantName())
                                        .sellingPrice(variant.getSellingPrice())
                                        .mrp(variant.getMrp())
                                        .cost(variant.getCost())
                                        .sku(variant.getSku())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        // Map each menu item to FeaturedOrderMenuResponse
        return orderMenuItemDtoList.stream()
                .filter(item -> item.getIsFeatured() != null && item.getIsFeatured() == 1)
                .map(item -> FeaturedOrderMenuResponse.builder()
                        .menuItemId(item.getId())
                        .menuItemKey(item.getMenuItemKey())
                        .menuItemName(item.getName())
                        .menuItemSlug(item.getSlug())
                        .menuItemShortDescription(item.getShortDescription())
                        .menuItemDescription(item.getDescription())
                        .menuItemIngredients(item.getIngredients())
                        .menuItemNutritionInfo(item.getNutritionInfo())
                        .menuItemImage(item.getImage())

                        .menuItemCategoryId(item.getCategoryId())
                        .menuItemCategoryKey(null) // Optional: Set if you have this info
                        .menuItemCategoryName(null)
                        .menuItemCategorySlug(null)
                        .menuItemCategoryDescription(null)
                        .menuItemCategoryMetaDescription(null)

                        .priceVariants(variantsByMenuItemId.getOrDefault(item.getId(), Collections.emptyList()))
                        .build())
                .collect(Collectors.toList());
    }
}

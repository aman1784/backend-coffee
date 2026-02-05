package com.coffee.responseMapper;

import com.coffee.dto.CreateOrderItemDto;
import com.coffee.dto.OrderMenuItemDto;
import com.coffee.dto.OrderMenuItemVariantDto;
import com.coffee.dto.PersistCartDto;
import com.coffee.request.CartOrderItemRequest;
import com.coffee.request.PersistCartRequest;
import com.coffee.response.GetCartResponse;
import com.coffee.response.PersistCartOrderItemResponse;
import com.coffee.response.PersistCartResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PersistCartResponseMapper {

    public List<PersistCartDto> updatePersistCart(PersistCartRequest request,
                                                  List<PersistCartDto> existingPersistCartDtoList) {
        // Default behavior: Replace/Sync (isAppendMode = false)
        return updatePersistCart(request, existingPersistCartDtoList, false);
    }

    // persistCartDtoList lenge (agar present hai toh), then check krenge kya incoming request se saari values match kr rhi hain?
    // if koi value nhi match kr rhi hain and vo DB mein hai, usse update kr denge -> check with itemId
    // if new items present hain in incoming request mein, then usse insert kr denge DB mein -> check with itemId (if not present in DB)
    // if DB mein item present hai but incoming request mein wo item nhi aaya, then uss item k status (set 0) in the DB -> check with itemId
    public List<PersistCartDto> updatePersistCart(PersistCartRequest request,
                                                  List<PersistCartDto> existingPersistCartDtoList,
                                                  boolean isAppendMode) {

        String userId = request.getUserId();
        OffsetDateTime now = OffsetDateTime.now();

        // 1. Build a map of existing cart items for the user, with only active items (status = 1)
        Map<String, PersistCartDto> existingItemMap = buildExistingItemMap(existingPersistCartDtoList, userId);

        // 2. Keep track of itemIds from incoming request to later find missing ones
        // 2. Keep track of composite keys from incoming request
        Set<String> incomingItemKeys = new HashSet<>();

        // 3. List to collect final updated items (updated, new, and soft-deleted)
        List<PersistCartDto> finalCartList = new ArrayList<>();

        // 4. Process each item from the incoming request
        for (CartOrderItemRequest incomingItem : request.getOrderItems()) {
            Long itemId = incomingItem.getItemId();
            Long variantId = incomingItem.getVariantId();
            String compositeKey = buildCompositeKey(itemId, variantId);

            incomingItemKeys.add(compositeKey); // track the itemId

            PersistCartDto existingItem = existingItemMap.get(compositeKey);

            if (existingItem != null) {
                // If an item already exists in DB, check for changes
                if (isItemChanged(existingItem, incomingItem)) {
                    // Update the existing item with new values and timestamp
                    updateExistingItem(existingItem, incomingItem, now);
                }
                finalCartList.add(existingItem); // add to result list (updated or not)
            } else {
                // New item isn't found in DB, so create a new one
                PersistCartDto newItem = createNewItem(userId, incomingItem, now);
                finalCartList.add(newItem);
            }
        }

        // 5. Find and mark items that exist in DB but are missing in the incoming request
        for (PersistCartDto existingItem : existingPersistCartDtoList) {
            if (existingItem.getUserId().equals(userId) && existingItem.getStatus() == 1) {
                String key = buildCompositeKey(existingItem.getItemId(), existingItem.getVariantId());
                // Mark as inactive (soft delete)
//                if (!incomingItemKeys.contains(key)) {
//                    markItemAsInactive(existingItem, now);
//                    finalCartList.add(existingItem);
//                }
                if (!incomingItemKeys.contains(key)) {
                    if (isAppendMode) {
                        // REORDER LOGIC: Keep the existing item in the list and active
                        finalCartList.add(existingItem);
                    } else {
                        // CART SYNC LOGIC: Mark as inactive because it was removed by user
                        markItemAsInactive(existingItem, now);
                        finalCartList.add(existingItem);
                    }
                }
            }
        }

        // 6. Return the full updated list
        return finalCartList;
    }


    // Builds a map of itemId to PersistCartDto for active items of the user
//    private Map<String, PersistCartDto> buildExistingItemMap(List<PersistCartDto> dtoList, String userId) {
//        return dtoList.stream()
//                .filter(dto -> dto.getUserId().equals(userId) && dto.getStatus() == 1)
//                .collect(Collectors.toMap(
//                        dto -> buildCompositeKey(dto.getItemId(), dto.getVariantId()),
//                        dto -> dto
//                ));
//    }
    private Map<String, PersistCartDto> buildExistingItemMap(List<PersistCartDto> dtoList, String userId) {
        return dtoList.stream()
                .filter(dto -> dto.getUserId().equals(userId) && dto.getStatus() == 1)
                .collect(Collectors.toMap(
                        dto -> buildCompositeKey(dto.getItemId(), dto.getVariantId()),
                        Function.identity(),
                        (existing, duplicate) -> existing   // 👈 prevents crash
                ));
    }

    private String buildCompositeKey(Long itemId, Long variantId) {
        return itemId + "-" + variantId;
    }


    // Compares relevant fields to determine if the item has changed
    private boolean isItemChanged(PersistCartDto existing, CartOrderItemRequest incoming) {
        return !Objects.equals(existing.getItemName(), incoming.getItemName()) ||
                compareBigDecimal(existing.getItemQuantity(), incoming.getQuantity()) != 0 ||
                compareBigDecimal(existing.getItemPrice(), incoming.getPrice()) != 0 ||
                compareBigDecimal(existing.getItemDiscount(), incoming.getDiscount()) != 0 ||
                !Objects.equals(existing.getVariantId(), incoming.getVariantId()) ||
                !Objects.equals(existing.getVariantName(), incoming.getVariantName());
    }

    // Compares two BigDecimals safely, handling nulls
    private int compareBigDecimal(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    // Updates an existing item with new values and sets the updated timestamp
    private void updateExistingItem(PersistCartDto existing, CartOrderItemRequest incoming, OffsetDateTime now) {
        existing.setItemName(incoming.getItemName());
        existing.setItemQuantity(incoming.getQuantity());
        existing.setItemPrice(incoming.getPrice());
        existing.setItemDiscount(incoming.getDiscount());
        existing.setVariantId(incoming.getVariantId());
        existing.setVariantName(incoming.getVariantName());
        existing.setMenuItemKey(incoming.getMenuItemKey());
        existing.setUpdatedAt(now);
    }

    // Creates a new PersistCartDto for a new cart item
    private PersistCartDto createNewItem(String userId, CartOrderItemRequest incoming, OffsetDateTime now) {
        return PersistCartDto.builder()
                .id(null)
                .userId(userId)
                .itemId(incoming.getItemId())
                .itemName(incoming.getItemName())
                .itemQuantity(incoming.getQuantity())
                .itemPrice(incoming.getPrice())
                .itemDiscount(incoming.getDiscount())
                .status(1)
                .createdAt(now)
                .updatedAt(now)
                .variantId(incoming.getVariantId())
                .variantName(incoming.getVariantName())
                .menuItemKey(incoming.getMenuItemKey())
                .build();
    }

    // Marks a DB item as inactive (soft delete) and sets the updated timestamp
    private void markItemAsInactive(PersistCartDto item, OffsetDateTime now) {
        item.setStatus(0);
        item.setUpdatedAt(now);
    }


    public GetCartResponse mapToGetCartResponse(List<PersistCartDto> existingPersistCartDtoList, String userId) {
        if (existingPersistCartDtoList == null || existingPersistCartDtoList.isEmpty()) {
            return GetCartResponse.builder()
                    .userId(userId)
                    .totalNumberOfItemsPresent(0)
                    .cartItems(Collections.emptyList())
                    .build();
        }

        List<PersistCartOrderItemResponse> cartItems = existingPersistCartDtoList.stream()
                .map(dto -> PersistCartOrderItemResponse.builder()
                        .itemId(dto.getItemId())
                        .itemName(dto.getItemName())
                        .quantity(dto.getItemQuantity())
                        .price(dto.getItemPrice())
                        .discount(dto.getItemDiscount())
                        .variantId(dto.getVariantId())
                        .variantName(dto.getVariantName())
                        .menuItemKey(dto.getMenuItemKey())
                        .build())
                .collect(Collectors.toList());

        int totalNumberOfItems = existingPersistCartDtoList.stream()
                .map(PersistCartDto::getItemQuantity)
                .filter(Objects::nonNull)
                .mapToInt(BigDecimal::intValue)
                .sum();


        return GetCartResponse.builder()
                .userId(userId)
                .totalNumberOfItemsPresent(totalNumberOfItems)
                .cartItems(cartItems)
                .build();
    }


    public List<PersistCartDto> insertPersistCart(PersistCartRequest request) {
        List<PersistCartDto> cartDtoList = new ArrayList<>();

        if (request.getOrderItems() != null) {
            for (CartOrderItemRequest itemRequest : request.getOrderItems()) {
                PersistCartDto dto = PersistCartDto.builder()
                        .id(null) // Assuming it's generated later (e.g., by DB)
                        .userId(request.getUserId())
                        .itemId(itemRequest.getItemId())
                        .itemName(itemRequest.getItemName())
                        .itemQuantity(itemRequest.getQuantity())
                        .itemPrice(itemRequest.getPrice())
                        .itemDiscount(itemRequest.getDiscount())
                        .status(1) // Assuming 1 = active
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .variantId(itemRequest.getVariantId())
                        .variantName(itemRequest.getVariantName())
                        .menuItemKey(itemRequest.getMenuItemKey())
                        .build();

                cartDtoList.add(dto);
            }
        }

        return cartDtoList;
    }

    public PersistCartResponse mapToPersistCartResponse(List<PersistCartDto> existingPersistCartDtoList, String userId) {
        if (existingPersistCartDtoList == null || existingPersistCartDtoList.isEmpty()) {
            return PersistCartResponse.builder()
                    .userId(userId)
                    .cartItems(Collections.emptyList())
                    .build();
        }

        List<PersistCartOrderItemResponse> cartItems = existingPersistCartDtoList.stream()
                .filter(dto -> dto.getStatus() == 1)
                .map(dto -> PersistCartOrderItemResponse.builder()
                        .itemId(dto.getItemId())
                        .itemName(dto.getItemName())
                        .quantity(dto.getItemQuantity())
                        .price(dto.getItemPrice())
                        .discount(dto.getItemDiscount())
                        .variantId(dto.getVariantId())
                        .variantName(dto.getVariantName())
                        .menuItemKey(dto.getMenuItemKey())
                        .build())
                .collect(Collectors.toList());

        return PersistCartResponse.builder()
                .userId(userId)
                .cartItems(cartItems)
                .build();
    }

    public PersistCartRequest mapToPersistCartRequestForReorder(
            String userId,
            List<OrderMenuItemDto> orderMenuItemDtoList,
            List<OrderMenuItemVariantDto> orderMenuItemVariantDtoList,
            List<CreateOrderItemDto> createOrderItemDtoList) {

        if (userId == null || userId.isBlank()) {
            return null;
        }

        List<CreateOrderItemDto> orderItems =
                Optional.ofNullable(createOrderItemDtoList).orElse(Collections.emptyList());

        Map<Long, OrderMenuItemDto> menuItemMap =
                Optional.ofNullable(orderMenuItemDtoList)
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(i -> i.getId() != null)
                        .collect(Collectors.toMap(
                                OrderMenuItemDto::getId,
                                Function.identity(),
                                (a, b) -> a
                        ));

        Map<Long, OrderMenuItemVariantDto> variantMap =
                Optional.ofNullable(orderMenuItemVariantDtoList)
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(v -> v.getId() != null)
                        .collect(Collectors.toMap(
                                OrderMenuItemVariantDto::getId,
                                Function.identity(),
                                (a, b) -> a
                        ));

        List<CartOrderItemRequest> cartItems = orderItems.stream()
                .filter(Objects::nonNull)
                .filter(item -> menuItemMap.containsKey(item.getItemId()))
                .filter(item ->
                        item.getVariantId() == null ||
                                variantMap.containsKey(item.getVariantId()))
                .map(item -> CartOrderItemRequest.builder()
                        .itemId(item.getItemId())
                        .itemName(item.getItemName())
                        .quantity(
                                Optional.ofNullable(item.getItemQuantity())
                                        .orElse(BigDecimal.ONE))
                        .price(
                                Optional.ofNullable(item.getItemPrice())
                                        .orElse(BigDecimal.ZERO))
                        .discount(
                                Optional.ofNullable(item.getItemDiscount())
                                        .orElse(BigDecimal.ZERO))
                        .variantId(item.getVariantId())
                        .variantName(item.getVariantName())
                        .menuItemKey(item.getMenuItemKey())
                        .build())
                .toList();

        return PersistCartRequest.builder()
                .userId(userId)
                .orderItems(cartItems)
                .build();
    }

}

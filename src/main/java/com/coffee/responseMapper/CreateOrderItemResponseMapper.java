package com.coffee.responseMapper;

import com.coffee.dto.CreateOrderDto;
import com.coffee.dto.CreateOrderItemDto;
import com.coffee.request.CreateOrderItemRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Aman Kumar Seth
 */

@Component
public class CreateOrderItemResponseMapper {

    public List<CreateOrderItemDto> mapToCreateOrderItemDto(CreateOrderDto createOrderDto,
                                                            List<CreateOrderItemRequest> orderItems, OffsetDateTime OffsetDateTime) {

        return orderItems.stream().map(orderItem -> CreateOrderItemDto.builder()
                .id(null)
                .orderId(createOrderDto.getOrderId())
                .itemName(orderItem.getItemName())
                .itemQuantity(orderItem.getItemQuantity())
                .itemPrice(orderItem.getItemPrice())
                .itemDiscount(orderItem.getItemDiscount())
                .status(1)
                .createdAt(OffsetDateTime)
                .updatedAt(OffsetDateTime)
                .itemId(orderItem.getItemId())
                .itemTotal(BigDecimal.ZERO)
                .variantId(orderItem.getVariantId())
                .variantName(orderItem.getVariantName())
                .menuItemKey(orderItem.getMenuItemKey())
                .build()).toList();
    }

    // This caused in the item total bug, when the same same item of different variant was added
//    public List<CreateOrderItemDto> setPriceDetailsInOrderItems(List<CreateOrderItemDto> createOrderItemDtoList,
//                                                                List<CreateOrderItemDto> setItemValues) {
//
//        createOrderItemDtoList.forEach(createOrderItemDto ->
//                createOrderItemDto.setItemTotal(setItemValues.stream().filter(item -> item.getItemName().equals(createOrderItemDto.getItemName())).findFirst().get().getItemTotal()));
//        return createOrderItemDtoList;
//    }

    // Map Key: Combines itemName and variantId into a unique key (e.g. "Espresso_2").
    // Benefit: Lookup becomes O(1) per item, vs. O(n) with .stream().filter()....
    // Safety: Avoids incorrect matches
    public List<CreateOrderItemDto> setPriceDetailsInOrderItems(List<CreateOrderItemDto> createOrderItemDtoList,
                                                                List<CreateOrderItemDto> setItemValues) {

        // Create a map with composite key: itemName + variantId
        Map<String, CreateOrderItemDto> itemValueMap = setItemValues.stream()
                .collect(Collectors.toMap(
                        item -> item.getItemName() + "_" + item.getVariantId(),
                        item -> item
                ));

        // Use the map to set itemTotal efficiently
        for (CreateOrderItemDto dto : createOrderItemDtoList) {
            String key = dto.getItemName() + "_" + dto.getVariantId();
            CreateOrderItemDto matchedItem = itemValueMap.get(key);
            if (matchedItem != null) {
                dto.setItemTotal(matchedItem.getItemTotal());
            }
        }

        return createOrderItemDtoList;
    }
}

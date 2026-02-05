package com.coffee.responseMapper;

import com.coffee.dto.PersistCartDto;
import com.coffee.dto.ShareCartLinkDto;
import com.coffee.dto.ShareCartLinkItemDto;
import com.coffee.projection.CheckUserProjection;
import com.coffee.response.CreateShareCartLinkResponse;
import com.coffee.response.ShareCartDetailItemResponse;
import com.coffee.response.ShareCartDetailsResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShareCartLinkResponseMapper {

    public ShareCartLinkDto createShareCartLinkDtoFromCreateShareCartLinkRequest(CheckUserProjection checkUserProjection) {

        return ShareCartLinkDto.builder()
                .id(null)
                .shareCartLink(createUniqueShareCartLink(checkUserProjection))
                .createdByUserId(checkUserProjection.getUserId())
                .createdBy(checkUserProjection.getFullName())
                .status(1)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    private String createUniqueShareCartLink(CheckUserProjection checkUserProjection) {
        String rawToken = checkUserProjection.getUserKey()
                + ":" + checkUserProjection.getUserId()
                + ":" + UUID.randomUUID()
                + ":" + System.currentTimeMillis();

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(rawToken.getBytes(StandardCharsets.UTF_8));
    }

    public CreateShareCartLinkResponse createShareCartLinkResponse(ShareCartLinkDto shareCartLinkDto) {

        return CreateShareCartLinkResponse.builder()
                .shareCartLink(shareCartLinkDto.getShareCartLink())
                .createdByUserName(shareCartLinkDto.getCreatedBy())
                .build();
    }

    public List<ShareCartLinkItemDto> insertIntoShareCartLinkItemDto(ShareCartLinkDto shareCartLinkDto,
                                                                     List<PersistCartDto> persistCartDtoList) {
        if (shareCartLinkDto == null || persistCartDtoList == null || persistCartDtoList.isEmpty()) {
            return Collections.emptyList();
        }

        return persistCartDtoList.stream()
                .map(persistCart -> ShareCartLinkItemDto.builder()
                        .id(null)
                        .shareCartLinkId(shareCartLinkDto.getId())
                        .createdByUserId(shareCartLinkDto.getCreatedByUserId())
                        .itemId(persistCart.getItemId())
                        .itemName(persistCart.getItemName())
                        .itemQuantity(persistCart.getItemQuantity())
                        .itemPrice(persistCart.getItemPrice())
                        .itemDiscount(persistCart.getItemDiscount())
                        .variantId(persistCart.getVariantId())
                        .variantName(persistCart.getVariantName())
                        .menuItemKey(persistCart.getMenuItemKey())
                        .status(1)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build())
                .collect(Collectors.toList());
    }

    public ShareCartDetailsResponse mapToGetShareCartDetailsResponse(List<ShareCartLinkItemDto> shareCartLinkItemDtoList,
                                                                     ShareCartLinkDto shareCartLinkDto) {

        // Handle null list safely
        if (shareCartLinkItemDtoList == null || shareCartLinkItemDtoList.isEmpty()) {
            return ShareCartDetailsResponse.builder()
                    .createdByUserId(shareCartLinkDto != null ? shareCartLinkDto.getCreatedByUserId() : null)
                    .createdByUserName(shareCartLinkDto != null ? shareCartLinkDto.getCreatedBy() : null)
                    .totalNumberOfItemsPresent(0)
                    .cartItems(Collections.emptyList())
                    .build();
        }

        // Map cart items (O(n))
        List<ShareCartDetailItemResponse> cartItems = shareCartLinkItemDtoList.stream()
                .filter(Objects::nonNull)
                .map(item -> ShareCartDetailItemResponse.builder()
                        .itemId(item.getItemId())
                        .itemName(item.getItemName())
                        .quantity(item.getItemQuantity())
                        .price(item.getItemPrice())
                        .discount(item.getItemDiscount())
                        .variantId(item.getVariantId())
                        .variantName(item.getVariantName())
                        .menuItemKey(item.getMenuItemKey())
                        .build())
                .collect(Collectors.toList());

        int totalNumberOfItems = shareCartLinkItemDtoList.stream()
                .map(ShareCartLinkItemDto::getItemQuantity)
                .filter(Objects::nonNull)
                .mapToInt(BigDecimal::intValue)
                .sum();

        return ShareCartDetailsResponse.builder()
                .createdByUserId(shareCartLinkDto != null ? shareCartLinkDto.getCreatedByUserId() : null)
                .createdByUserName(shareCartLinkDto != null ? shareCartLinkDto.getCreatedBy() : null)
                .totalNumberOfItemsPresent(totalNumberOfItems)
                .cartItems(cartItems)
                .build();
    }

}

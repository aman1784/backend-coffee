package com.coffee.usecase.impl;

import com.coffee.dto.OrderMenuItemDto;
import com.coffee.dto.OrderMenuItemVariantDto;
import com.coffee.dto.UserFavoriteProductDto;
import com.coffee.exception.GenericException;
import com.coffee.request.UserFavoriteProductRequest;
import com.coffee.response.UserFavoriteProductResponse;
import com.coffee.responseMapper.UserFavoriteProductResponseMapper;
import com.coffee.service.OrderMenuCategoryService;
import com.coffee.service.OrderMenuItemService;
import com.coffee.service.OrderMenuItemVariantService;
import com.coffee.service.UserFavoriteProductService;
import com.coffee.usecase.UserFavoriteProductUseCaseService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @version 1.0
 * @since 2025-11-28
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UserFavoriteProductUseCaseServiceImpl implements UserFavoriteProductUseCaseService {

    @Lazy
    private final UserFavoriteProductService userFavoriteProductService;
    @Lazy
    private final OrderMenuItemService orderMenuItemService;
    @Lazy
    private final OrderMenuItemVariantService orderMenuItemVariantService;
    @Lazy
    private final OrderMenuCategoryService orderMenuCategoryService;

    private final UserFavoriteProductResponseMapper userFavoriteProductResponseMapper;

//    @Cacheable(value = "getFavoriteProductForUser")
    @Override
    public UserFavoriteProductResponse getFavoriteProductForUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User id is required");
        }

        // Fetch the favorite products for the user from the database
        List<UserFavoriteProductDto> userFavoriteProductDtoList = userFavoriteProductService.findAllByUserId(userId);
        log.debug("[UserFavoriteProductUseCaseService][getFavoriteProductForUser] userFavoriteProductDtoList: {}", userFavoriteProductDtoList);

        if (userFavoriteProductDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "No favorite products found for the user");
        }

        List<String> menuItemKeys = userFavoriteProductDtoList.stream().map(UserFavoriteProductDto::getMenuItemKey).distinct().toList();
        log.debug("[UserFavoriteProductUseCaseService][getFavoriteProductForUser] menuItemKeys: {}", menuItemKeys);

        List<OrderMenuItemDto> orderMenuItemDtoList = orderMenuItemService.findByMenuItemKeyIn(menuItemKeys);
        log.debug("[UserFavoriteProductUseCaseService][getFavoriteProductForUser] orderMenuItemDtoList: {}", orderMenuItemDtoList);

        if (orderMenuItemDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "No order menu items found for the favorite products");
        }

        List<String> variantKeys = userFavoriteProductDtoList.stream().map(UserFavoriteProductDto::getVariantKey).distinct().toList();
        log.debug("[UserFavoriteProductUseCaseService][getFavoriteProductForUser] variantKeys: {}", variantKeys);

        List<OrderMenuItemVariantDto> orderMenuItemVariantDtoList = orderMenuItemVariantService.findAllByVariantKeyIn(variantKeys);
        log.debug("[UserFavoriteProductUseCaseService][getFavoriteProductForUser] orderMenuItemVariantDtoList: {}", orderMenuItemVariantDtoList);

        if (orderMenuItemVariantDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "No order menu item variants found for the favorite products");
        }

        UserFavoriteProductResponse response = userFavoriteProductResponseMapper.buildUserFavoriteProductResponse(userId, userFavoriteProductDtoList, orderMenuItemDtoList, menuItemKeys, orderMenuItemVariantDtoList, variantKeys);
        log.debug("[UserFavoriteProductUseCaseService][getFavoriteProductForUser] response: {}", response);
        return response;
    }

    @Override
    public String addToFavorites(UserFavoriteProductRequest request) {
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User id is required");
        }
        if (request.getMenuItemKey() == null || request.getMenuItemKey().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Menu item key is required");
        }
        if (request.getVariantKey() == null || request.getVariantKey().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Variant key is required");
        }

        UserFavoriteProductDto userFavoriteProductDto = userFavoriteProductResponseMapper.mapToUserFavoriteProductDto(request);
        log.debug("[UserFavoriteProductUseCaseService][addToFavorites][mapToUserFavoriteProductDto] userFavoriteProductDto: {}", userFavoriteProductDto);
        userFavoriteProductDto = userFavoriteProductService.save(userFavoriteProductDto);
        log.debug("[UserFavoriteProductUseCaseService][addToFavorites][save] userFavoriteProductDto: {}", userFavoriteProductDto);
        return "Favorite product added successfully";
    }

    @Override
    public String removeFromFavorites(UserFavoriteProductRequest request) {
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User id is required");
        }
        if (request.getMenuItemKey() == null || request.getMenuItemKey().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Menu item key is required");
        }
        if (request.getVariantKey() == null || request.getVariantKey().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Variant key is required");
        }

        UserFavoriteProductDto userFavoriteProductDto = userFavoriteProductResponseMapper.mapToUserFavoriteProductDto(request);
        log.debug("[UserFavoriteProductUseCaseService][removeFromFavorites][mapToUserFavoriteProductDto] userFavoriteProductDto: {}", userFavoriteProductDto);
        userFavoriteProductDto = userFavoriteProductService.findByUserIdAndMenuItemKeyAndVariantKey(userFavoriteProductDto.getUserId(), userFavoriteProductDto.getMenuItemKey(), userFavoriteProductDto.getVariantKey());
        log.debug("[UserFavoriteProductUseCaseService][removeFromFavorites][findByUserIdAndMenuItemKeyAndVariantKey] userFavoriteProductDto: {}", userFavoriteProductDto);
        if (userFavoriteProductDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Favorite product not found");
        }

        boolean isDeleted = false;
        log.debug("isDeleted: {}", isDeleted);
        isDeleted = userFavoriteProductService.delete(userFavoriteProductDto);
        log.debug("[UserFavoriteProductUseCaseService][removeFromFavorites][delete] userFavoriteProductDto: {}", isDeleted);
        if (isDeleted) {
            return "Favorite product removed successfully";
        }

        throw new GenericException(ExceptionConstant.otherError, "Something went wrong", ExceptionConstant.otherErrorMessage, "Failed to remove favorite product, please try again");
    }
}

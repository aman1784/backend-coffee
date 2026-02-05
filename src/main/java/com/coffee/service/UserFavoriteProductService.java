package com.coffee.service;

import com.coffee.dto.UserFavoriteProductDto;

import java.util.List;

public interface UserFavoriteProductService {
    
    UserFavoriteProductDto save(UserFavoriteProductDto dto);

    List<UserFavoriteProductDto> findAllByUserId(String userId);

    UserFavoriteProductDto findByUserIdAndMenuItemKeyAndVariantKey(String userId, String menuItemKey, String variantKey);

    boolean delete(UserFavoriteProductDto userFavoriteProductDto);
}

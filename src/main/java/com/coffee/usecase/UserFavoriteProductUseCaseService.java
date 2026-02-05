package com.coffee.usecase;

import com.coffee.request.UserFavoriteProductRequest;
import com.coffee.response.UserFavoriteProductResponse;

public interface UserFavoriteProductUseCaseService {
    UserFavoriteProductResponse getFavoriteProductForUser(String userId);

    String addToFavorites(UserFavoriteProductRequest request);

    String removeFromFavorites(UserFavoriteProductRequest request);
}

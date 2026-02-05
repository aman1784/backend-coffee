package com.coffee.controller;

import com.coffee.request.UserFavoriteProductRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.UserFavoriteProductResponse;
import com.coffee.usecase.UserFavoriteProductUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @version 1.0
 * @since 2025-11-28
 */

@RestController
@RequestMapping(path = "/api/v1.0/user-favorite-product")
@Slf4j
@RequiredArgsConstructor
public class UserFavoriteProductController {

    private final UserFavoriteProductUseCaseService service;

    @GetMapping(value = "/favorites/{userId}")
    public ResponseEntity<BaseResponse<UserFavoriteProductResponse>> getFavoriteProductForUser(@PathVariable String userId) {
        log.debug("[UserFavoriteProductController][getFavorites] request: userId: {}", userId);
        UserFavoriteProductResponse response = service.getFavoriteProductForUser(userId);
        log.debug("[UserFavoriteProductController][getFavorites] response: {}", response);
        BaseResponse<UserFavoriteProductResponse> baseResponse = BaseResponse.<UserFavoriteProductResponse>builder().data(response).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/add-to-favorites")
    public ResponseEntity<BaseResponse<String>> addToFavorites(@RequestBody UserFavoriteProductRequest request) {
        log.debug("[UserFavoriteProductController][addToFavorites] request: {}", request);
        String response = service.addToFavorites(request);
        log.debug("[UserFavoriteProductController][addToFavorites] response: {}", response);
        BaseResponse<String> baseResponse = BaseResponse.<String>builder().data(response).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/remove-from-favorites")
    public ResponseEntity<BaseResponse<String>> removeFromFavorites(@RequestBody UserFavoriteProductRequest request) {
        log.debug("[UserFavoriteProductController][removeFromFavorites] request: {}", request);
        String response = service.removeFromFavorites(request);
        log.debug("[UserFavoriteProductController][removeFromFavorites] response: {}", response);
        BaseResponse<String> baseResponse = BaseResponse.<String>builder().data(response).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}

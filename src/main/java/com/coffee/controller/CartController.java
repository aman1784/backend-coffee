package com.coffee.controller;

import com.coffee.request.PersistCartRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.GetCartResponse;
import com.coffee.response.PersistCartResponse;
import com.coffee.usecase.PersistCartUseCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/cart")
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final PersistCartUseCaseService service;
    
    @PostMapping(value = "/persistCart")
    public ResponseEntity<BaseResponse<PersistCartResponse>> persistCart(@RequestBody @Valid PersistCartRequest request) {
        log.debug("[CreateOrderController][persistCart] request: {}", request);
        BaseResponse<PersistCartResponse> response = service.persistCart(request);
        log.debug("[CreateOrderController][persistCart] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/getCartDetail")
    public ResponseEntity<BaseResponse<GetCartResponse>> getCartDetail(@RequestParam(value = "userId") String userId) {
        log.debug("[CreateOrderController][getCart] request: userId: {}", userId);
        BaseResponse<GetCartResponse> response = service.getCartDetail(userId);
        log.debug("[CreateOrderController][getCart] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

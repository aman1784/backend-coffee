package com.coffee.usecase.impl;

import com.coffee.dto.PersistCartDto;
import com.coffee.exception.GenericException;
import com.coffee.projection.CheckUserIdProjection;
import com.coffee.request.PersistCartRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.GetCartResponse;
import com.coffee.response.PersistCartResponse;
import com.coffee.responseMapper.PersistCartResponseMapper;
import com.coffee.service.PersistCartService;
import com.coffee.service.UsersService;
import com.coffee.usecase.PersistCartUseCaseService;
import com.coffee.util.ExceptionConstant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersistCartUseCaseServiceImpl implements PersistCartUseCaseService {

    private final UsersService usersService;
    private final PersistCartService persistCartService;

    private final PersistCartResponseMapper persistcartResponseMapper;

    @Transactional
    @Override
    public BaseResponse<PersistCartResponse> persistCart(PersistCartRequest request) {
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User id is required");
        }
        CheckUserIdProjection usersDto = usersService.findByUserId(request.getUserId());
        if (usersDto == null) { // Something wrong, correct user id required
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found");
        }
        List<PersistCartDto> existingPersistCartDtoList = persistCartService.findByUserId(usersDto.getUserId());
        if (existingPersistCartDtoList.isEmpty()) {
            existingPersistCartDtoList = persistcartResponseMapper.insertPersistCart(request);
            log.info("[CreateOrderUseCaseServiceImpl][persistCart] existingPersistCartDtoList inserted: {}", existingPersistCartDtoList);
        } else {
            // persistCartDtoList lenge (agar present hai toh), then check krenge kya incoming request se saari values match kr rhi hain?
            // if koi value nhi match kr rhi hain and vo DB mein hai, usse update kr denge -> check with itemId
            // if new items present hain in incoming request mein, then usse insert kr denge DB mein -> check with itemId (if not present in DB)
            // if DB mein item present hai but incoming request mein wo item nhi aaya, then uss item k status (set 0) in the DB -> check with itemId
            existingPersistCartDtoList = persistcartResponseMapper.updatePersistCart(request, existingPersistCartDtoList);
            log.info("[CreateOrderUseCaseServiceImpl][persistCart] existingPersistCartDtoList updated: {}", existingPersistCartDtoList);
        }
        existingPersistCartDtoList = persistCartService.saveAll(existingPersistCartDtoList);
        log.info("[CreateOrderUseCaseServiceImpl][persistCart] existingPersistCartDtoList saved in db: {}", existingPersistCartDtoList);
        PersistCartResponse response = persistcartResponseMapper.mapToPersistCartResponse(existingPersistCartDtoList, usersDto.getUserId());
        return BaseResponse.<PersistCartResponse>builder().data(response).build();
    }

    @Override
    public BaseResponse<GetCartResponse> getCartDetail(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User id is required");
        }
        CheckUserIdProjection usersDto = usersService.findByUserId(userId);
        if (usersDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found");
        }
        List<PersistCartDto> existingPersistCartDtoList = persistCartService.findByUserId(usersDto.getUserId());
        if (existingPersistCartDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "No Items in cart");
        }
        GetCartResponse response = persistcartResponseMapper.mapToGetCartResponse(existingPersistCartDtoList, usersDto.getUserId());
        return BaseResponse.<GetCartResponse>builder().data(response).build();
    }
}

package com.coffee.usecase.impl;

import com.coffee.dto.*;
import com.coffee.exception.GenericException;
import com.coffee.projection.CheckUserIdProjection;
import com.coffee.request.PersistCartRequest;
import com.coffee.request.ReorderRequest;
import com.coffee.response.BaseResponse;
import com.coffee.responseMapper.PersistCartResponseMapper;
import com.coffee.service.*;
import com.coffee.usecase.ReOrderUseCaseService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @version 1.0
 * @since 2025-11-28
 */

@RequiredArgsConstructor
@Slf4j
@Service
public class ReOrderUseCaseServiceImpl implements ReOrderUseCaseService {

    private final CreateOrderService createOrderService;
    private final CreateOrderItemService createOrderItemService;
    private final UsersService usersService;
    private final PersistCartService persistCartService;
    private final OrderMenuItemService orderMenuItemService;
    private final OrderMenuItemVariantService orderMenuItemVariantService;

    private final PersistCartResponseMapper persistcartResponseMapper;

    @Override
    public BaseResponse<String> reorder(ReorderRequest request) {
        CreateOrderDto createOrderDto = createOrderService.findByOrderIdAndOrderKey(request.getOrderId(), request.getOrderKey());
        log.info("[ReOrderUseCaseServiceImpl][reorder] createOrderDto: {}", createOrderDto);
        if (createOrderDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Order not found");
        }
        List<CreateOrderItemDto> createOrderItemDtoList = createOrderItemService.findAllByOrderId(createOrderDto.getOrderId());
        log.info("[ReOrderUseCaseServiceImpl][reorder] createOrderItemDtoList: {}", createOrderItemDtoList);
        if (createOrderItemDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Order items not found");
        }

        List<String> menuItemKeys = createOrderItemDtoList.stream().map(CreateOrderItemDto::getMenuItemKey).distinct().toList();
        log.info("[ReOrderUseCaseServiceImpl][reorder] menuItemKeys: {}", menuItemKeys);
        List<Long> variantIds = createOrderItemDtoList.stream().map(CreateOrderItemDto::getVariantId).distinct().toList();
        log.info("[ReOrderUseCaseServiceImpl][reorder] variantIds: {}", variantIds);

        List<OrderMenuItemDto> orderMenuItemDtoList = orderMenuItemService.findByMenuItemKeyIn(menuItemKeys);
        log.info("[ReOrderUseCaseServiceImpl][reorder] orderMenuItemDtoList: {}", orderMenuItemDtoList);

        List<OrderMenuItemVariantDto> orderMenuItemVariantDtoList = orderMenuItemVariantService.findAllByIdIn(variantIds);
        log.info("[ReOrderUseCaseServiceImpl][reorder] orderMenuItemVariantDtoList: {}", orderMenuItemVariantDtoList);

        CheckUserIdProjection usersDto = usersService.findByUserId(request.getUserId());
        if (usersDto == null) { // Something wrong, correct user id required
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found");
        }

        PersistCartRequest persistCartRequest = persistcartResponseMapper.mapToPersistCartRequestForReorder(usersDto.getUserId(), orderMenuItemDtoList, orderMenuItemVariantDtoList, createOrderItemDtoList);
        log.info("[ReOrderUseCaseServiceImpl][reorder] persistCartRequest: {}", persistCartRequest);

        List<PersistCartDto> existingPersistCartDtoList = persistCartService.findByUserId(request.getUserId());
        if (existingPersistCartDtoList.isEmpty()) {
            existingPersistCartDtoList = persistcartResponseMapper.insertPersistCart(persistCartRequest);
            log.info("[ReOrderUseCaseServiceImpl][reorder] existingPersistCartDtoList inserted: {}", existingPersistCartDtoList);
        } else {
            // persistCartDtoList lenge (agar present hai toh), then check krenge kya incoming request se saari values match kr rhi hain?
            // if koi value nhi match kr rhi hain and vo DB mein hai, usse update kr denge -> check with itemId
            // if new items present hain in incoming request mein, then usse insert kr denge DB mein -> check with itemId (if not present in DB)
            // if DB mein item present hai but incoming request mein wo item nhi aaya, then uss item k status (set 0) in the DB -> check with itemId
            existingPersistCartDtoList = persistcartResponseMapper.updatePersistCart(persistCartRequest, existingPersistCartDtoList, true);
            log.info("[ReOrderUseCaseServiceImpl][reorder] existingPersistCartDtoList updated: {}", existingPersistCartDtoList);
        }
        existingPersistCartDtoList = persistCartService.saveAll(existingPersistCartDtoList);
        log.info("[ReOrderUseCaseServiceImpl][reorder] existingPersistCartDtoList saved in db: {}", existingPersistCartDtoList);
        return BaseResponse.<String>builder().data("Your Reorder has been successfully placed in the cart.").build();
    }
}

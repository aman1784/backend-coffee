package com.coffee.usecase.impl;

import com.coffee.dto.CreateOrderDto;
import com.coffee.dto.CreateOrderItemDto;
//import com.coffee.dto.OrderItemRatingDto;
import com.coffee.dto.UsersDto;
import com.coffee.exception.GenericException;
import com.coffee.helperClass.OrderCalculationResult;
// import com.coffee.razorpay.RazorPayService;
import com.coffee.request.CreateOrderItemRequest;
import com.coffee.request.CreateOrderRequest;
import com.coffee.request.CreatePaymentLinkRequest;
import com.coffee.request.GetAllOrdersRequest;
import com.coffee.response.*;
import com.coffee.responseMapper.CreateOrderItemResponseMapper;
import com.coffee.responseMapper.CreateOrderResponseMapper;
import com.coffee.service.CreateOrderItemService;
import com.coffee.service.CreateOrderService;
//import com.coffee.service.OrderItemRatingService;
import com.coffee.service.UsersService;
import com.coffee.usecase.CreateOrderUseCaseService;
import com.coffee.usecase.PaymentUseCaseService;
import com.coffee.util.DateTimeConverterUtil;
import com.coffee.util.ExceptionConstant;
import com.coffee.validator.CreateOrderRequestValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Aman Kumar Seth
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CreateOrderUseCaseServiceImpl implements CreateOrderUseCaseService {

    private final CreateOrderResponseMapper createOrderResponseMapper;
    private final CreateOrderItemResponseMapper createOrderItemResponseMapper;

    private final CreateOrderService createOrderService;
    private final CreateOrderItemService createOrderItemService;
    private final UsersService usersService;
    // private final RazorPayService razorPayService;
    private final PaymentUseCaseService paymentUseCaseService;
    private final ThreadPoolExecutor threadPoolExecutor;
//    private final OrderItemRatingService orderItemRatingService;
    private final CacheManager cacheManager; // used for manual cache

    private final CreateOrderRequestValidator createOrderItemRequestValidator;

    @Transactional
    @Override
    @CacheEvict(value = "getAllOrders", allEntries = true) // this will remove the getAllOrders so to fetch latest data after new order created
    public BaseResponse<CreateOrderResponse> createOrder(CreateOrderRequest request) {
        validateCustomerInformation(request);
        createOrderItemRequestValidator.validateOrderItems(request.getOrderItems());
        OffsetDateTime OffsetDateTime = java.time.OffsetDateTime.now(); // it will be used for createdAt and updatedAt to make it consistent
        CreateOrderDto createOrderDto = createOrderResponseMapper.mapToCreateOrderDto(request, OffsetDateTime);
        log.info("[CreateOrderUseCaseServiceImpl][createOrder] createOrderDto: {}", createOrderDto);
        List<CreateOrderItemDto> createOrderItemDtoList = createOrderItemResponseMapper.mapToCreateOrderItemDto(createOrderDto, request.getOrderItems(), OffsetDateTime);
        log.info("[CreateOrderUseCaseServiceImpl][createOrder] createOrderItemDtoList: {}", createOrderItemDtoList);
        OrderCalculationResult orderCalculationResult = createOrderResponseMapper.calculateOrder(createOrderDto, createOrderItemDtoList);
        log.info("[CreateOrderUseCaseServiceImpl][createOrder] orderCalculationResult: {}", orderCalculationResult);

        // Async call to create razorpay payment link
        String paymentLink = "";
        if (request.getPaymentType() != null && request.getPaymentType().equalsIgnoreCase("online")) {
            CreatePaymentLinkRequest paymentLinkRequest = CreatePaymentLinkRequest.builder().userId(createOrderDto.getUserId()).cartId("").orderId(createOrderDto.getOrderId()).paymentType(request.getPaymentType())
                    .grandTotal(createOrderDto.getTotal()).customerName(createOrderDto.getCustomerName()).customerPhoneNumber(request.getCustomerPhoneNumber()).orderKey(createOrderDto.getOrderKey()).build();
            log.debug("[CreateOrderUseCaseServiceImpl][createOrder][Async] paymentLinkRequest: {}", paymentLinkRequest);
            paymentLink = createPaymentLink(paymentLinkRequest);
        }

        createOrderDto = createOrderResponseMapper.setPriceDetailsInOrder(createOrderDto, orderCalculationResult.getOrder(), paymentLink);
        createOrderDto = createOrderService.save(createOrderDto);
        log.info("[CreateOrderUseCaseServiceImpl][createOrder] saved in db createOrderDto: {}", createOrderDto);
        createOrderItemDtoList = createOrderItemResponseMapper.setPriceDetailsInOrderItems(createOrderItemDtoList, orderCalculationResult.getItems());
        log.info("[CreateOrderUseCaseServiceImpl][createOrder] all values set createOrderItemDtoList: {}", createOrderItemDtoList);
        createOrderItemDtoList = createOrderItemService.saveAll(createOrderItemDtoList);
        log.info("[CreateOrderUseCaseServiceImpl][createOrder] saved in db createOrderItemDtoList: {}", createOrderItemDtoList);
//        List<OrderItemRatingDto> orderItemRatingDto = createOrderResponseMapper.mapToOrderItemRatingDto(request, createOrderDto);
//        log.info("[CreateOrderUseCaseServiceImpl][createOrder] orderItemRatingDto: {}", orderItemRatingDto);
//        orderItemRatingDto = orderItemRatingService.saveAll(orderItemRatingDto);
//        log.info("[CreateOrderUseCaseServiceImpl][createOrder] saved in db orderItemRatingDto: {}", orderItemRatingDto);
        CreateOrderResponse response = createOrderResponseMapper.mapToCreateOrderResponse(createOrderDto, paymentLink);
//        Objects.requireNonNull(cacheManager.getCache("orders")).put(response.getOrderKey(), response); // manual cache
        return BaseResponse.<CreateOrderResponse>builder().data(response).build();
    }
    private String createPaymentLink(CreatePaymentLinkRequest paymentLinkRequest) {
        Future<String> future = threadPoolExecutor.submit(() -> {
            try {
                BaseResponse<CreatePaymentLinkResponse> response = paymentUseCaseService.createPaymentLink(paymentLinkRequest);
                log.info("[CreateOrderUseCaseServiceImpl][createPaymentLink][Async] Payment link created successfully: {}", response);
                return response.getData().getLink();
            } catch (Exception e) {
                log.error("[CreateOrderUseCaseServiceImpl][createPaymentLink][Async] Error while creating payment link", e);
                return ""; // fallback
            }
        });

        try {
            return future.get(); // This blocks until the task completes
        } catch (InterruptedException | ExecutionException e) {
            log.debug("[CreateOrderUseCaseServiceImpl][createPaymentLink][Async] Debug retrieving payment link from future", e);
            log.error("[CreateOrderUseCaseServiceImpl][createPaymentLink][Async] Error retrieving payment link from future", e);
            return "";
        }
    }

    private void validateCustomerInformation(CreateOrderRequest request) {
        UsersDto user = usersService.findByUserIdAndFullNameAndPhoneNumber(request.getUserId(), request.getCustomerName(), request.getCustomerPhoneNumber());
        if (user == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found");
        }
    }

    @Override
    @Cacheable(value = "getOrder", key = "#orderId")
    public BaseResponse<GetOrderResponse> getOrder(String orderId, String orderKey) {
        if (orderId == null || orderId.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Order id is required");
        }
        if (orderKey == null || orderKey.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Order key is required");
        }
        CreateOrderDto createOrderDto = createOrderService.findByOrderIdAndOrderKey(orderId, orderKey);
        log.info("[CreateOrderUseCaseServiceImpl][getOrder] createOrderDto: {}", createOrderDto);
        if (createOrderDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Order not found");
        }
        List<CreateOrderItemDto> createOrderItemDtoList = createOrderItemService.findAllByOrderId(createOrderDto.getOrderId());
        log.info("[CreateOrderUseCaseServiceImpl][getOrder] createOrderItemDtoList: {}", createOrderItemDtoList);
        if (createOrderItemDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Order items not found");
        }
        GetOrderResponse response = createOrderResponseMapper.mapToGetOrderResponse(createOrderDto, createOrderItemDtoList);
        return BaseResponse.<GetOrderResponse>builder().data(response).build();
    }

    @Cacheable(value = "getAllOrders")
    @Override
    public BaseResponse<GetAllOrdersResponse> getAllOrders(int page, int size, String sort, String userId, String startDate, String endDate) {
        if (userId == null || userId.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User id is required");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<CreateOrderDto> createOrderDtoPage = createOrderService.findByUserId(userId, pageable, startDate, endDate);
        log.info("[CreateOrderUseCaseServiceImpl][getAllOrders] createOrderDtoPage: {}", createOrderDtoPage);
        if (createOrderDtoPage == null || createOrderDtoPage.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Orders not found");
        }
        List<String> orderIds = createOrderDtoPage.getContent().stream().map(CreateOrderDto::getOrderId).collect(Collectors.toList());
        List<CreateOrderItemDto> createOrderItemDtoList = createOrderItemService.findAllByOrderIdIn(orderIds);
        log.info("[CreateOrderUseCaseServiceImpl][getAllOrders] createOrderItemDtoList: {}", createOrderItemDtoList);
        if (createOrderItemDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Order items not found");
        }
        GetAllOrdersResponse response = createOrderResponseMapper.mapToGetAllOrdersResponse(createOrderDtoPage, createOrderItemDtoList);
        return BaseResponse.<GetAllOrdersResponse>builder().data(response).build();
    }

}
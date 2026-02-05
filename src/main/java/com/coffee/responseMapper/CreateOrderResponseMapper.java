package com.coffee.responseMapper;

import com.coffee.dto.CreateOrderDto;
import com.coffee.dto.CreateOrderItemDto;
import com.coffee.dto.OrderItemRatingDto;
import com.coffee.enums.OrderStatus;
import com.coffee.enums.PaymentStatus;
import com.coffee.helperClass.OrderCalculationResult;
import com.coffee.request.CreateOrderItemRequest;
import com.coffee.request.CreateOrderRequest;
import com.coffee.response.CreateOrderItemResponse;
import com.coffee.response.CreateOrderResponse;
import com.coffee.response.GetAllOrdersResponse;
import com.coffee.response.GetOrderResponse;
import com.coffee.util.CreateOrderUtility;
import com.coffee.util.DateTimeConverterUtil;
import com.coffee.util.Utility;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Aman Kumar Seth
 */

@Component
public class CreateOrderResponseMapper {

    public CreateOrderDto mapToCreateOrderDto(CreateOrderRequest request, OffsetDateTime OffsetDateTime) {

        return CreateOrderDto.builder()
                .id(null)
                .orderId(CreateOrderUtility.createOrderId())
                .orderKey(Utility.generateMd5Hash(request.getCustomerName()+request.getCustomerPhoneNumber()+request.getUserId()+"_"+(System.currentTimeMillis() % 10000)))
                .customerName(request.getCustomerName())
                .customerPhoneNumber(request.getCustomerPhoneNumber())
                .subTotal(BigDecimal.ZERO)
                .gst(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .status(1)
                .createdAt(OffsetDateTime)
                .updatedAt(OffsetDateTime)
                .userId(request.getUserId())
                .orderStatus(OrderStatus.ORDER_PLACED.getValue())
                .paymentStatus(PaymentStatus.PENDING.getValue())
                .paymentType(request.getPaymentType() != null && request.getPaymentType().equalsIgnoreCase("online") ? "Online" : "Cash")
                .build();
    }

    public OrderCalculationResult calculateOrder(CreateOrderDto createOrderDto, List<CreateOrderItemDto> createOrderItemDtoList) {
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal totalItemDiscount = BigDecimal.ZERO;

        for (CreateOrderItemDto item : createOrderItemDtoList) {
            BigDecimal quantity = safe(item.getItemQuantity());
            BigDecimal price = safe(item.getItemPrice());
            BigDecimal itemDiscount = safe(item.getItemDiscount());

            BigDecimal totalItemPrice = quantity.multiply(price);
            BigDecimal totalDiscount = quantity.multiply(itemDiscount);
            BigDecimal itemTotal = totalItemPrice.subtract(totalDiscount);

            subTotal = subTotal.add(itemTotal);
            totalItemDiscount = totalItemDiscount.add(totalDiscount);

            // ✅ Set itemTotal per item
            item.setItemTotal(scale(itemTotal));
        }

        // GST (e.g., 18%)
        BigDecimal gstRate = new BigDecimal("0.18");
        BigDecimal gst = subTotal.multiply(gstRate);

        // Order-level discount (optional)
        BigDecimal orderDiscount = safe(createOrderDto.getDiscount()).add(totalItemDiscount);

        // Final total
        BigDecimal total = subTotal.add(gst).subtract(orderDiscount);

        // Set calculated values into the DTO
        createOrderDto.setSubTotal(scale(subTotal));
        createOrderDto.setGst(scale(gst));
        createOrderDto.setDiscount(scale(orderDiscount));
        createOrderDto.setTotal(scale(total));

        return new OrderCalculationResult(createOrderDto, createOrderItemDtoList);
    }

    // Safely return the BigDecimal value or ZERO if null
    private BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    // Round to 2 decimal places using HALF_UP rounding mode
    private BigDecimal scale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }


    public CreateOrderDto setPriceDetailsInOrder(CreateOrderDto createOrderDto, CreateOrderDto order, String paymentLink) {
        createOrderDto.setSubTotal(order.getSubTotal());
        createOrderDto.setGst(order.getGst());
        createOrderDto.setDiscount(order.getDiscount());
        createOrderDto.setTotal(order.getTotal());
        createOrderDto.setPaymentLink(paymentLink);
        return createOrderDto;
    }

    public GetAllOrdersResponse mapToGetAllOrdersResponse(Page<CreateOrderDto> createOrderDtoPage,
                                                          List<CreateOrderItemDto> createOrderItemDtoList) {

        // Group items by orderId for quick access
        Map<String, List<CreateOrderItemDto>> itemsByOrderId = createOrderItemDtoList.stream()
                .collect(Collectors.groupingBy(CreateOrderItemDto::getOrderId));

        List<GetOrderResponse> orders = createOrderDtoPage.getContent().stream()
                .map(order -> {
                    List<CreateOrderItemDto> items = itemsByOrderId.getOrDefault(order.getOrderId(), Collections.emptyList());

                    // Map each item to CreateOrderItemResponse (currently only itemName is mapped)
                    List<CreateOrderItemResponse> orderItems = items.stream()
                            .map(item -> CreateOrderItemResponse.builder()
                                    .orderItemName(createOrderItemName(item.getItemName(), item.getVariantName())) // item.getItemName() + " ( " + item.getVariantName() + " )")
                                    .build())
                            .collect(Collectors.toList());

                    // Calculate GST price
                    BigDecimal gstPrice = order.getSubTotal()
                            .multiply(order.getGst().divide(BigDecimal.valueOf(100)));

                    return GetOrderResponse.builder()
                            .orderId(order.getOrderId())
                            .orderKey(order.getOrderKey())
                            .customerName(order.getCustomerName())
                            .customerPhoneNumber(order.getCustomerPhoneNumber())
                            .subTotal(order.getSubTotal())
                            .gstPercentage(order.getGst().toPlainString())
                            .gstPrice(gstPrice)
                            .discount(order.getDiscount())
                            .total(order.getTotal())
                            .status(order.getStatus())
//                            .createdAt(order.getCreatedAt().atOffset(ZoneOffset.UTC))
//                            .createdAt(order.getCreatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime()) // "createdAt": "2025-09-17T12:10:47+05:30",
//                            .createdAt(order.getCreatedAt().atZone(ZoneId.of("Asia/Kolkata")).toOffsetDateTime()) // "createdAt": "2025-09-17T12:10:47+05:30",
                            //.createdAt(DateTimeConverterUtil.convertToISTDateTimeStringFromUTCDateTimeString(order.getCreatedAt()))
                            .createdAt(order.getCreatedAt())
                            .userId(order.getUserId())
                            .orderItems(orderItems)
                            .paymentLink(null) // If needed, set from elsewhere
                            .orderStatus(order.getOrderStatus())
                            .paymentStatus(order.getPaymentStatus())
                            .paymentType(order.getPaymentType())
                            .build();
                })
                .collect(Collectors.toList());

        return GetAllOrdersResponse.builder()
                .orders(orders)
                .page(String.valueOf(createOrderDtoPage.getNumber()))
                .size(String.valueOf(createOrderDtoPage.getSize()))
                .totalPages(String.valueOf(createOrderDtoPage.getTotalPages()))
                .totalElements(String.valueOf(createOrderDtoPage.getTotalElements()))
                .build();
    }

    private String createOrderItemName(String itemName, String variantName) {
        if (variantName == null || variantName.isEmpty()) {
            return itemName;
        }
        return itemName + " ( " + variantName + " )";
    }


    public CreateOrderResponse mapToCreateOrderResponse(CreateOrderDto createOrderDto, String paymentLink) {
        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderId(createOrderDto.getOrderId());
        response.setOrderKey(createOrderDto.getOrderKey());
        response.setCustomerName(createOrderDto.getCustomerName());
        response.setCustomerPhoneNumber(createOrderDto.getCustomerPhoneNumber());
        response.setTotal(createOrderDto.getTotal());
        // response.setCreatedAt(DateTimeConverterUtil.convertToISTDateTimeStringFromUTCDateTimeString(createOrderDto.getCreatedAt()));
        response.setCreatedAt(createOrderDto.getCreatedAt());
        response.setUserId(createOrderDto.getUserId());
        response.setOrderMessage("Order created successfully");
        response.setPaymentLink(paymentLink);
        return response;
    }

    public GetOrderResponse mapToGetOrderResponse(CreateOrderDto createOrderDto, List<CreateOrderItemDto> createOrderItemDtoList) {

        GetOrderResponse response = new GetOrderResponse();
        response.setOrderId(createOrderDto.getOrderId());
        response.setOrderKey(createOrderDto.getOrderKey());
        response.setCustomerName(createOrderDto.getCustomerName());
        response.setCustomerPhoneNumber(createOrderDto.getCustomerPhoneNumber());
        response.setSubTotal(createOrderDto.getSubTotal());
        response.setGstPercentage("18%");
        response.setGstPrice(createOrderDto.getGst());
        response.setDiscount(createOrderDto.getDiscount());
        response.setTotal(createOrderDto.getTotal());
        response.setStatus(createOrderDto.getStatus());
//        response.setCreatedAt(DateTimeConverterUtil.convertToISTDateTimeStringFromUTCDateTimeString(createOrderDto.getCreatedAt()));
        response.setCreatedAt(createOrderDto.getCreatedAt());
        response.setUserId(createOrderDto.getUserId());
        List<CreateOrderItemResponse> orderItemsResponseList = new ArrayList<>();
        for (CreateOrderItemDto item : createOrderItemDtoList) {
            CreateOrderItemResponse orderItemResponse = CreateOrderItemResponse.builder()
                    .orderId(item.getOrderId())
                    .orderItemName(createOrderItemName(item.getItemName(), item.getVariantName())) // item.getItemName() + " ( " + item.getVariantName() + " )")
                    .orderItemPrice(item.getItemPrice())
                    .orderItemQuantity(item.getItemQuantity())
                    .orderItemDiscount(item.getItemDiscount())
                    .orderItemTotalPrice(item.getItemTotal())
                    .menuItemKey(item.getMenuItemKey())
                    .build();
            orderItemsResponseList.add(orderItemResponse);
        }
        response.setOrderItems(orderItemsResponseList);
        response.setPaymentLink(createOrderDto.getPaymentLink() != null ? createOrderDto.getPaymentLink() : "");
        response.setOrderStatus(createOrderDto.getOrderStatus());
        response.setPaymentStatus(createOrderDto.getPaymentStatus());
        response.setPaymentType(createOrderDto.getPaymentType());
        return response;
    }

    public List<OrderItemRatingDto> mapToOrderItemRatingDto(CreateOrderRequest request, CreateOrderDto createOrderDto) {

        List<OrderItemRatingDto> orderItemRatingDtoList = new ArrayList<>();

        for (CreateOrderItemRequest item : request.getOrderItems()) {

            OrderItemRatingDto orderItemRatingDto = OrderItemRatingDto.builder()
                    .id(null)
                    .orderId(createOrderDto.getOrderId())
                    .menuItemKey(item.getMenuItemKey())
                    .rating(0)
                    .comment(null)
                    .image(null)
                    .status(1)
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .userName(createOrderDto.getCustomerName())
                    .build();

            orderItemRatingDtoList.add(orderItemRatingDto);
        }

        return orderItemRatingDtoList;
    }
}

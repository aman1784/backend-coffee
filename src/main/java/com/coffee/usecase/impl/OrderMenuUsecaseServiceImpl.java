package com.coffee.usecase.impl;

import com.coffee.dto.OrderItemRatingDto;
import com.coffee.dto.OrderMenuCategoryDto;
import com.coffee.dto.OrderMenuItemDto;
import com.coffee.dto.OrderMenuItemVariantDto;
import com.coffee.exception.GenericException;
import com.coffee.request.OrderMenuCategoryFilterRequest;
import com.coffee.response.*;
import com.coffee.responseMapper.OrderMenuResponseMapper;
import com.coffee.service.OrderItemRatingService;
import com.coffee.service.OrderMenuCategoryService;
import com.coffee.service.OrderMenuItemService;
import com.coffee.service.OrderMenuItemVariantService;
import com.coffee.usecase.OrderMenuUsecaseService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Objects;

/**
 * @author Aman Kumar Seth
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderMenuUsecaseServiceImpl implements OrderMenuUsecaseService {

    private final OrderMenuItemService orderMenuItemService;
    private final OrderMenuCategoryService orderMenuCategoryService;
    private final OrderMenuResponseMapper orderMenuResponseMapper;
    private final OrderMenuItemVariantService OrderMenuItemVariantService;
    private final OrderItemRatingService orderItemRatingService;

    @Override
    public byte[] getAllMenuDetailsExcelSheet() {
        List<OrderMenuItemDto> orderMenuItemDtos = orderMenuItemService.findAll();
        log.debug("[OrderMenuUsecaseServiceImpl][getAllMenuDetailsExcelSheet] orderMenuItemDtos: {}", orderMenuItemDtos);
        return createCsvFile(orderMenuItemDtos);
    }

    private byte[] createCsvFile(List<OrderMenuItemDto> orderMenuItemDtos) {
        String[] headers = {"id", "Category Id", "Name", "Short Description", "Description", "Image"};

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(bos, "UTF-8");
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            // Write header
            bufferedWriter.write(String.join(",", headers));
            bufferedWriter.newLine();

            if (orderMenuItemDtos != null && !orderMenuItemDtos.isEmpty()) {
                // Write rows
                for (OrderMenuItemDto dto: orderMenuItemDtos) {
                    String row = String.join(",",
                            quote(dto.getId() != null ? dto.getId().toString() : ""),
                            quote(dto.getCategoryId() != null ? dto.getCategoryId().toString() : ""),
                            quote(dto.getName() != null ? dto.getName() : ""),
                            quote(dto.getShortDescription() != null ? dto.getShortDescription() : ""),
                            quote(dto.getDescription() != null ? dto.getDescription() : ""),
                            quote(dto.getImage() != null ? dto.getImage() : "")
                    );
                    bufferedWriter.write(row);
                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.flush();
            return bos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("[OrderMenuUsecaseServiceImpl][createCsvFile] Error while creating CSV file", e);
        }
    }

    /**
     * Helper to safely quote CSV values if they contain commas/quotes/newlines
     */
    private String quote(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\""); // escape double quotes
            return "\"" + value + "\"";
        }
        return value;
    }

    @Cacheable(value = "featuredOrderMenu")
    @Override
    public BaseResponse<List<FeaturedOrderMenuResponse>> featuredOrderMenu() {
        List<OrderMenuItemDto> orderMenuItemDtoList = orderMenuItemService.findAllByIsFeatured();
        log.debug("[OrderMenuUsecaseServiceImpl][featuredOrderMenu] orderMenuItemDtoList: {}", orderMenuItemDtoList);
        if (orderMenuItemDtoList.isEmpty() || orderMenuItemDtoList == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "No Featured Menu Items found");
        }
        List<Long> menuItemIds = orderMenuItemDtoList.stream()
                .map(OrderMenuItemDto::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        log.debug("[OrderMenuUsecaseServiceImpl][featuredOrderMenu] menuItemIds: {}", menuItemIds);
        List<OrderMenuItemVariantDto> OrderMenuItemVariantDtos = OrderMenuItemVariantService.findAllByMenuItemIdIn(menuItemIds);
        log.debug("[OrderMenuUsecaseServiceImpl][featuredOrderMenu] OrderMenuItemVariantDtos: {}", OrderMenuItemVariantDtos);
        List<FeaturedOrderMenuResponse> response = orderMenuResponseMapper.buildFeaturedOrderMenuResponse(orderMenuItemDtoList, OrderMenuItemVariantDtos);
        return BaseResponse.<List<FeaturedOrderMenuResponse>>builder().data(response).build();
    }

    @Override
//    @Cacheable(value = "listOfCategories")
    public BaseResponse<List<OrderMenuCategoryResponse>> listOfCategories() {
        // when LOGIN user is ADMIN -> show all categories EVEN INACTIVE
        // NEED TO IMPLEMENT THIS LOGIC
        List<OrderMenuCategoryDto> dtos = orderMenuCategoryService.findAll();
        if (dtos.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Menu Categories not found");
        }
        List<OrderMenuCategoryResponse> response = orderMenuResponseMapper.buildListOfCategoriesResponse(dtos);
        return BaseResponse.<List<OrderMenuCategoryResponse>>builder().data(response).build();
    }

    @Cacheable(value = "getAllOrderMenu")
    @Override
    public BaseResponse<OrderMenuItemPageResponse> getAllOrderMenu(int page, int size, OrderMenuCategoryFilterRequest request) {

        Pageable pageable = PageRequest.of(page, size); //, Sort.by(Sort.Direction.DESC, "id"));

        Page<OrderMenuItemDto> orderMenuItemDtos = null;
        List<OrderMenuCategoryDto> orderMenuCategoryDtos = null;

        if (request.getCategoryKey() != null && !request.getCategoryKey().isEmpty()) { // when category is selected
            List<String> categoryKeys = request.getCategoryKey();
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu][with category] categoryKeys: {}", categoryKeys);
            orderMenuCategoryDtos = orderMenuCategoryService.findAllByCategoryKeyIn(categoryKeys);
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu][with category] orderMenuCategoryDtos: {}", orderMenuCategoryDtos);
            List<Long> categoryIds = orderMenuCategoryDtos.stream()
                    .map(OrderMenuCategoryDto::getId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu][with category] categoryIds: {}", categoryIds);
            orderMenuItemDtos = orderMenuItemService.findAllByCategoryIds(categoryIds, pageable);
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu][with category] orderMenuDtos: {}", orderMenuItemDtos);
        } else { // when category is not selected
            orderMenuCategoryDtos = orderMenuCategoryService.findAll();
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu][without category] orderMenuCategoryDtos: {}", orderMenuCategoryDtos);
            orderMenuItemDtos = orderMenuItemService.getAllOrderMenu(pageable);
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu][without category] orderMenuDtos: {}", orderMenuItemDtos);
        }

        if (orderMenuItemDtos == null || orderMenuItemDtos.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Menu Items Not Found");
        }

        List<OrderMenuItemVariantDto> OrderMenuItemVariantDtos = null;
        List<OrderItemRatingDto> orderItemRatingDtos = null;
        if (orderMenuItemDtos != null && !orderMenuItemDtos.isEmpty()) {
            List<Long> menuItemIds = orderMenuItemDtos.stream()
                    .map(OrderMenuItemDto::getId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu] menuItemIds: {}", menuItemIds);
            OrderMenuItemVariantDtos = OrderMenuItemVariantService.findAllByMenuItemIdIn(menuItemIds);
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu] OrderMenuItemVariantDtos: {}", OrderMenuItemVariantDtos);

            List<String> menuItemKeys = orderMenuItemDtos.stream()
                    .map(OrderMenuItemDto::getMenuItemKey)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu] menuItemKeys: {}", menuItemKeys);
            orderItemRatingDtos = orderItemRatingService.findAllByMenuItemKeyIn(menuItemKeys);
            log.debug("[OrderMenuUsecaseServiceImpl][getAllOrderMenu] orderItemRatingDtos: {}", orderItemRatingDtos);
        }

        assert OrderMenuItemVariantDtos != null;
        OrderMenuItemPageResponse response = orderMenuResponseMapper.buildOrderMenuPageResponse(orderMenuItemDtos, orderMenuCategoryDtos, OrderMenuItemVariantDtos, orderItemRatingDtos);
        return BaseResponse.<OrderMenuItemPageResponse>builder().data(response).build();
    }

    @Override
    public BaseResponse<OrderMenuItemResponse> getMenuItemDetail(String menuItemKey) {
        OrderMenuItemDto orderMenuItemDto = orderMenuItemService.findByMenuItemKey(menuItemKey);
        log.debug("[OrderMenuUsecaseServiceImpl][getMenuItemDetail] orderMenuItemDto: {}", orderMenuItemDto);

        if (orderMenuItemDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Menu Item Not Found");
        }

        OrderMenuCategoryDto orderMenuCategoryDto = orderMenuCategoryService.findById(orderMenuItemDto.getCategoryId());
        log.debug("[OrderMenuUsecaseServiceImpl][getMenuItemDetail] orderMenuCategoryDto: {}", orderMenuCategoryDto);

        // ✅ Fetch all price variants for this item
        List<OrderMenuItemVariantDto> priceVariants = OrderMenuItemVariantService.findAllByMenuItemId(orderMenuItemDto.getId());
        log.debug("[OrderMenuUsecaseServiceImpl][getMenuItemDetail] priceVariants: {}", priceVariants);

        // ✅ Fetch all ratings for this item
        List<OrderItemRatingDto> orderItemRatingDtos = orderItemRatingService.findAllByMenuItemKey(orderMenuItemDto.getMenuItemKey());
        log.debug("[OrderMenuUsecaseServiceImpl][getMenuItemDetail] orderItemRatingDtos: {}", orderItemRatingDtos);

        // ✅ Pass the full list to the response mapper
        OrderMenuItemResponse response = orderMenuResponseMapper.buildOrderMenuItemResponse(orderMenuItemDto, orderMenuCategoryDto, priceVariants, orderItemRatingDtos);

        return BaseResponse.<OrderMenuItemResponse>builder().data(response).build();
    }


    @Override
    public BaseResponse<OrderMenuSearchResponse> getSearchWord(String searchWord) {
        List<OrderMenuItemDto> orderMenuItemDtos = orderMenuItemService.getAllBySearchWord(searchWord);
        log.debug("[OrderMenuUsecaseServiceImpl][getSearchWord] orderMenuItemDtos: {}", orderMenuItemDtos);

        List<OrderMenuCategoryDto> orderMenuCategoryDtos;
        List<OrderMenuItemVariantDto> OrderMenuItemVariantDtos = null;
        if (!orderMenuItemDtos.isEmpty()) {
            List<Long> categoryIds = orderMenuItemDtos.stream()
                    .map(OrderMenuItemDto::getCategoryId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            log.debug("[OrderMenuUsecaseServiceImpl][getSearchWord] categoryIds: {}", categoryIds);

            orderMenuCategoryDtos = orderMenuCategoryService.getAllByCategoryIdInOrSearchWord(categoryIds, searchWord);
            log.debug("[OrderMenuUsecaseServiceImpl][getSearchWord] orderMenuCategoryDtos: {}", orderMenuCategoryDtos);
            List<Long> menuItemIds = orderMenuItemDtos.stream()
                    .map(OrderMenuItemDto::getId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            log.debug("[OrderMenuUsecaseServiceImpl][getSearchWord] menuItemIds: {}", menuItemIds);
            OrderMenuItemVariantDtos = OrderMenuItemVariantService.findAllByMenuItemIdIn(menuItemIds);
            log.debug("[OrderMenuUsecaseServiceImpl][getSearchWord] OrderMenuItemVariantDtos: {}", OrderMenuItemVariantDtos);
        } else {
            orderMenuCategoryDtos = orderMenuCategoryService.getAllBySearchWord(searchWord);
        }


        assert OrderMenuItemVariantDtos != null;
        OrderMenuSearchResponse response = orderMenuResponseMapper.buildOrderMenuSearchResponse(orderMenuItemDtos, orderMenuCategoryDtos, OrderMenuItemVariantDtos);
        return BaseResponse.<OrderMenuSearchResponse>builder().data(response).build();
    }

//    @Transactional
//    @Override
//    public BaseResponse<AddItemInMenuAndCategoryResponse> addItemInMenuAndCategory(AddItemInMenuAndCategoryRequest request) {
//        addItemInMenuAndCategoryValidator(request);
//
//        // check if ADMIN (MANAGER) IS ADDING THE ITEM
//        // NEED TO CHECK IF USER IS ADMIN OR NOT
//        // NEED TO IMPLEMENT THIS LOGIC
//        OrderMenuCategoryDto orderMenuCategoryDto = orderMenuCategoryService.findByIdAndNameIrrespectiveOfStatus(request.getMenuCategory().getCategoryId(), request.getMenuCategory().getName());
//        if (orderMenuCategoryDto == null) {
//            orderMenuCategoryDto = orderMenuResponseMapper.buildOrderMenuCategoryDtoFromAddMenuCategory(request);
//            log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategory] orderMenuCategoryDto: {}", orderMenuCategoryDto);
//            orderMenuCategoryDto = orderMenuCategoryService.create(orderMenuCategoryDto);
//            log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategory][create] orderMenuCategoryDto: {}", orderMenuCategoryDto);
//        }
//
//        OrderMenuItemDto orderMenuDto = orderMenuItemService.findByNameIrrespectiveOfStatus(request.getMenuItem().getName());
//        if (orderMenuDto != null){
//            throw new GenericException(ExceptionConstant.badRequestCode, "Menu Item already present", orderMenuDto.getName());
//        }
//        orderMenuDto = orderMenuResponseMapper.buildOrderMenuDtoFromAddMenuItem(request, orderMenuCategoryDto);
//        log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategory] orderMenuDto: {}", orderMenuDto);
//        orderMenuDto = orderMenuItemService.create(orderMenuDto);
//        log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategory][create] orderMenuDto: {}", orderMenuDto);
//
//        AddItemInMenuAndCategoryResponse response = orderMenuResponseMapper.buildAddItemInMenuAndCategoryResponse(orderMenuCategoryDto, orderMenuDto);
//
//        return BaseResponse.<AddItemInMenuAndCategoryResponse>builder().data(response).build();
//    }
//
//    private void addItemInMenuAndCategoryValidator(AddItemInMenuAndCategoryRequest request) {
//        if (request.getMenuCategory() == null) {
//            log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategoryValidator] request.getMenuCategory(): {}", (Object) null);
//            throw new GenericException(ExceptionConstant.badRequestCode, ExceptionConstant.badRequestMessage, "Menu Category cannot be null");
//        }
//        if (request.getMenuItem() == null) {
//            log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategoryValidator] request.getMenuItem(): {}", (Object) null);
//            throw new GenericException(ExceptionConstant.badRequestCode, ExceptionConstant.badRequestMessage, "Menu Item cannot be null");
//        }
//        if (request.getMenuCategory().getName() == null || request.getMenuCategory().getName().trim().isEmpty()) {
//            log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategoryValidator] request.getMenuCategory().getName(): {}", (Object) null);
//            throw new GenericException(ExceptionConstant.badRequestCode, ExceptionConstant.badRequestMessage, "Menu Category -> Name cannot be null or empty");
//        }
//        if (request.getMenuItem().getName() == null || request.getMenuItem().getName().trim().isEmpty()) {
//            log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategoryValidator] request.getMenuItem().getName(): {}", (Object) null);
//            throw new GenericException(ExceptionConstant.badRequestCode, ExceptionConstant.badRequestMessage, "Menu Item -> Name cannot be null or empty");
//        }
//        if (request.getMenuItem().getMrp() == null || request.getMenuItem().getMrp().compareTo(BigDecimal.ZERO) <= 0) {
//            log.debug("[OrderMenuUsecaseServiceImpl][addItemInMenuAndCategoryValidator] request.getMenuItem().getMrp(): {}", (Object) null);
//            throw new GenericException(ExceptionConstant.badRequestCode, ExceptionConstant.badRequestMessage, "Menu Item -> Mrp cannot be null or <= 0");
//        }
//    }

}

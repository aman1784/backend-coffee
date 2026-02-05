package com.coffee.service;

import com.coffee.dto.OrderMenuCategoryDto;

import java.util.List;

public interface OrderMenuCategoryService {

    List<OrderMenuCategoryDto> findAll();

    List<OrderMenuCategoryDto> getAllByStatus1();

    List<OrderMenuCategoryDto> getAllByCategoryIdInOrSearchWord(List<Long> categoryIds, String searchWord);

    List<OrderMenuCategoryDto> getAllBySearchWord(String searchWord);

    OrderMenuCategoryDto create(OrderMenuCategoryDto orderMenuCategoryDto);

    OrderMenuCategoryDto findByIdAndName(Long categoryId, String name);

    OrderMenuCategoryDto findByIdAndNameIrrespectiveOfStatus(Long categoryId, String name);

    List<OrderMenuCategoryDto> findAllByCategoryKeyIn(List<String> categoryKeys);

    OrderMenuCategoryDto findById(Long categoryId);
}

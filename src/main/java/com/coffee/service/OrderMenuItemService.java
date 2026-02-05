package com.coffee.service;

import com.coffee.dto.OrderMenuItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderMenuItemService {

    Page<OrderMenuItemDto> getAllOrderMenu(Pageable pageable);

    Page<OrderMenuItemDto> findAllByCategoryIds(List<Long> categoryIds, Pageable pageable);

    OrderMenuItemDto findByMenuItemKey(String menuItemKey);

    List<OrderMenuItemDto> getAllBySearchWord(String searchWord);

    List<OrderMenuItemDto> findAll();

    OrderMenuItemDto create(OrderMenuItemDto OrderMenuItemDto);

    OrderMenuItemDto findByNameIrrespectiveOfStatus(String name);

    OrderMenuItemDto findByName(String name);

    List<OrderMenuItemDto> findAllByIsFeatured();

    List<OrderMenuItemDto> findAllByName(String name);

    List<OrderMenuItemDto> findByMenuItemKeyIn(List<String> menuItemKeys);
}

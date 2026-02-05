package com.coffee.service;

import com.coffee.dto.OrderMenuItemVariantDto;

import java.util.List;

public interface OrderMenuItemVariantService {

    List<OrderMenuItemVariantDto> findAllByMenuItemIdIn(List<Long> menuItemIds);

    OrderMenuItemVariantDto findByMenuItemId(Long id);

    List<OrderMenuItemVariantDto> findAllByMenuItemId(Long id);

    List<OrderMenuItemVariantDto> findAllByVariantKeyIn(List<String> variantKeys);

    List<OrderMenuItemVariantDto> findAllByIdIn(List<Long> variantIds);

}

package com.coffee.service.impl;

import com.coffee.dto.OrderMenuItemVariantDto;
import com.coffee.entity.OrderMenuItemVariantEntity;
import com.coffee.entityMapper.OrderMenuItemVariantEntityMapper;
import com.coffee.repository.OrderMenuItemVariantRepository;
import com.coffee.service.OrderMenuItemVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderMenuItemVariantServiceImpl implements OrderMenuItemVariantService {

    private final OrderMenuItemVariantRepository repository;
    private final OrderMenuItemVariantEntityMapper mapper;

    @Override
    public List<OrderMenuItemVariantDto> findAllByMenuItemIdIn(List<Long> menuItemIds) {
        List<OrderMenuItemVariantEntity> entities = repository.findAllByMenuItemIdInAndStatus(menuItemIds, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public OrderMenuItemVariantDto findByMenuItemId(Long id) {
        OrderMenuItemVariantEntity entity = repository.findByMenuItemIdAndStatus(id, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public List<OrderMenuItemVariantDto> findAllByMenuItemId(Long id) {
        List<OrderMenuItemVariantEntity> entities = repository.findAllByMenuItemIdAndStatus(id, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderMenuItemVariantDto> findAllByVariantKeyIn(List<String> variantKeys) {
        List<OrderMenuItemVariantEntity> entities = repository.findAllByVariantKeyInAndStatus(variantKeys, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderMenuItemVariantDto> findAllByIdIn(List<Long> variantIds) {
        List<OrderMenuItemVariantEntity> entities = repository.findAllByIdInAndStatus(variantIds, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }
}

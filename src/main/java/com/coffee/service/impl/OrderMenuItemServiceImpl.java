package com.coffee.service.impl;

import com.coffee.dto.OrderMenuItemDto;
import com.coffee.entity.OrderMenuItemEntity;
import com.coffee.entityMapper.OrderMenuItemEntityMapper;
import com.coffee.repository.OrderMenuItemRepository;
import com.coffee.service.OrderMenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderMenuItemServiceImpl implements OrderMenuItemService {

    private final OrderMenuItemRepository repository;
    private final OrderMenuItemEntityMapper mapper;


    @Override
    public Page<OrderMenuItemDto> getAllOrderMenu(Pageable pageable) {
        Page<OrderMenuItemEntity> entities = repository.findAllByStatus(1, pageable);
        return entities.isEmpty() ? null : entities.map(mapper::buildDto);
    }

    @Override
    public Page<OrderMenuItemDto> findAllByCategoryIds(List<Long> categoryIds, Pageable pageable) {
        Page<OrderMenuItemEntity> entities = repository.findAllByCategoryIdInAndStatus(categoryIds, 1, pageable);
        return entities.isEmpty() ? null : entities.map(mapper::buildDto);
    }

    @Override
    public OrderMenuItemDto findByMenuItemKey(String menuItemKey) {
        OrderMenuItemEntity entity = repository.findByMenuItemKeyAndStatus(menuItemKey, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public List<OrderMenuItemDto> getAllBySearchWord(String searchWord) {
        List<OrderMenuItemEntity> entities = repository.findAllSearchWord(searchWord);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderMenuItemDto> findAll() {
        List<OrderMenuItemEntity> entities = repository.findAllByStatus(1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public OrderMenuItemDto create(OrderMenuItemDto OrderMenuItemDto) {
        OrderMenuItemEntity entity = mapper.buildEntity(OrderMenuItemDto);
        entity = repository.save(entity);
        return mapper.buildDto(entity);
    }

    @Override
    public OrderMenuItemDto findByNameIrrespectiveOfStatus(String name) {
        OrderMenuItemEntity entity = repository.findByName(name);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public OrderMenuItemDto findByName(String name) {
        OrderMenuItemEntity entity = repository.findByNameAndStatus(name, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public List<OrderMenuItemDto> findAllByIsFeatured() {
        List<OrderMenuItemEntity> entities = repository.findAllByIsFeaturedAndStatus(1, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderMenuItemDto> findAllByName(String name) {
        List<OrderMenuItemEntity> entities = repository.findAllByNameAndStatus(name, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderMenuItemDto> findByMenuItemKeyIn(List<String> menuItemKeys) {
        List<OrderMenuItemEntity> entities = repository.findByMenuItemKeyInAndStatus(menuItemKeys, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }
}

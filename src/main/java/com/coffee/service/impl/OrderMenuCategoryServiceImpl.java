package com.coffee.service.impl;

import com.coffee.dto.OrderMenuCategoryDto;
import com.coffee.entity.OrderMenuCategoryEntity;
import com.coffee.entityMapper.OrderMenuCategoryEntityMapper;
import com.coffee.repository.OrderMenuCategoryRepository;
import com.coffee.service.OrderMenuCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMenuCategoryServiceImpl implements OrderMenuCategoryService {

    private final OrderMenuCategoryRepository repository;
    private final OrderMenuCategoryEntityMapper mapper;

    @Override
    public List<OrderMenuCategoryDto> findAll() {
        List<OrderMenuCategoryEntity> entities = repository.findAllByStatus(1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderMenuCategoryDto> getAllByStatus1() {
        List<OrderMenuCategoryEntity> entities = repository.findAllByStatus(1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderMenuCategoryDto> getAllBySearchWord(String searchWord) {
        List<OrderMenuCategoryEntity> entities = repository.findAllSearchWord(searchWord);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderMenuCategoryDto> getAllByCategoryIdInOrSearchWord(List<Long> categoryIds, String searchWord) {
        List<OrderMenuCategoryEntity> entities = repository.getAllByCategoryIdInOrSearchWord(categoryIds, searchWord);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public OrderMenuCategoryDto create(OrderMenuCategoryDto orderMenuCategoryDto) {
        OrderMenuCategoryEntity entity = mapper.buildEntity(orderMenuCategoryDto);
        entity = repository.save(entity);
        return mapper.buildDto(entity);
    }

    @Override
    public OrderMenuCategoryDto findByIdAndName(Long categoryId, String name) {
        OrderMenuCategoryEntity entity = repository.findByIdAndNameAndStatus(categoryId, name, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public OrderMenuCategoryDto findByIdAndNameIrrespectiveOfStatus(Long categoryId, String name) {
        OrderMenuCategoryEntity entity = repository.findByIdAndName(categoryId, name);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public List<OrderMenuCategoryDto> findAllByCategoryKeyIn(List<String> categoryKeys) {
        List<OrderMenuCategoryEntity> entities = repository.findAllByCategoryKeyInAndStatus(categoryKeys, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public OrderMenuCategoryDto findById(Long categoryId) {
        OrderMenuCategoryEntity entity = repository.findByIdAndStatus(categoryId, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }
}

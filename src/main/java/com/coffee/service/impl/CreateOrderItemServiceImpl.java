package com.coffee.service.impl;

import com.coffee.dto.CreateOrderItemDto;
import com.coffee.entity.CreateOrderItemEntity;
import com.coffee.entityMapper.CreateOrderItemEntityMapper;
import com.coffee.repository.CreateOrderItemRepository;
import com.coffee.service.CreateOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderItemServiceImpl implements CreateOrderItemService {

    private final CreateOrderItemRepository repository;
    private final CreateOrderItemEntityMapper mapper;

    @Override
    public List<CreateOrderItemDto> saveAll(List<CreateOrderItemDto> createOrderItemDtoList) {
        return mapper.buildDtos(repository.saveAll(mapper.buildEntities(createOrderItemDtoList)));
    }

    @Override
    public List<CreateOrderItemDto> findAllByOrderId(String orderId) {
        List<CreateOrderItemEntity> entities = repository.findAllByOrderIdAndStatus(orderId, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }

    @Override
    public List<CreateOrderItemDto> findAllByOrderIdIn(List<String> orderIds) {
        List<CreateOrderItemEntity> entities = repository.findAllByOrderIdInAndStatus(orderIds, 1);
        return entities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(entities);
    }
}

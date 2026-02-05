package com.coffee.service.impl;

import com.coffee.dto.OrderItemRatingDto;
import com.coffee.entity.OrderItemRatingEntity;
import com.coffee.entityMapper.OrderItemRatingEntityMapper;
import com.coffee.repository.OrderItemRatingRepository;
import com.coffee.service.OrderItemRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aman Kumar Seth
 */

@Service
@RequiredArgsConstructor
public class OrderItemRatingServiceImpl implements OrderItemRatingService {

    private final OrderItemRatingRepository repository;
    private final OrderItemRatingEntityMapper mapper;

    @Override
    public List<OrderItemRatingDto> saveAll(List<OrderItemRatingDto> orderItemRatingDto) {
        List<OrderItemRatingEntity> entities = mapper.buildEntities(orderItemRatingDto);
        entities = repository.saveAll(entities);
        return mapper.buildDtos(entities);
    }

    @Override
    public List<OrderItemRatingDto> findAllByOrderId(String orderId) {
        List<OrderItemRatingEntity> entities = repository.findAllByOrderIdAndStatus(orderId, 1);
        return entities.isEmpty() ? null : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderItemRatingDto> findAllByMenuItemKeyIn(List<String> menuItemKeys) {
        List<OrderItemRatingEntity> entities = repository.findAllByMenuItemKeyInAndStatus(menuItemKeys, 1);
        return entities.isEmpty() ? null : mapper.buildDtos(entities);
    }

    @Override
    public List<OrderItemRatingDto> findAllByMenuItemKey(String menuItemKey) {
        List<OrderItemRatingEntity> entities = repository.findAllByMenuItemKeyAndStatus(menuItemKey, 1);
        return entities.isEmpty() ? null : mapper.buildDtos(entities);
    }
}

package com.coffee.service.impl;

import com.coffee.dto.CreateOrderDto;
import com.coffee.entity.CreateOrderEntity;
import com.coffee.entityMapper.CreateOrderEntityMapper;
import com.coffee.exception.GenericException;
import com.coffee.repository.CreateOrderRepository;
import com.coffee.service.CreateOrderService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class CreateOrderServiceImpl implements CreateOrderService {

    private final CreateOrderRepository repository;
    private final CreateOrderEntityMapper mapper;

    @Override
    public CreateOrderDto save(CreateOrderDto createOrderDto) {
        CreateOrderEntity entity = mapper.buildEntity(createOrderDto);
        entity = repository.save(entity);
        return mapper.buildDto(entity);
    }

    @Override
    public CreateOrderDto findByOrderIdAndOrderKey(String orderId, String orderKey) {
        CreateOrderEntity entity = repository.findByOrderIdAndOrderKeyAndStatus(orderId, orderKey, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public Page<CreateOrderDto> findByUserIdAndCustomerNameAndCustomerPhoneNumber(String userId, String customerName, String customerPhoneNumber, Pageable pageable) {
        Page<CreateOrderEntity> entities = repository.findByUserIdAndCustomerNameAndCustomerPhoneNumberAndStatus(userId, customerName, customerPhoneNumber, 1, pageable);
        return entities.isEmpty() ? null : entities.map(mapper::buildDto);
    }

    @Override
    public Page<CreateOrderDto> findByUserId(String userId, Pageable pageable, String startDate, String endDate) {
        Page<CreateOrderEntity> entities = null;
        if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
           entities = repository.findByUserIdAndStatus(userId, 1, pageable);
        }
        else if ((startDate != null && !startDate.isEmpty()) && (endDate != null && !endDate.isEmpty()) && startDate.equalsIgnoreCase(endDate)) {
            LocalDate date = LocalDate.parse(startDate);   // "2023-09-28"
            OffsetDateTime startDateTime = date.atStartOfDay().atOffset(ZoneOffset.UTC);
            OffsetDateTime endDateTime   = date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

            entities = repository.findByUserIdAndStatusAndCreatedAtBetween(userId, 1, startDateTime, endDateTime, pageable);
        }
        else if ((startDate != null && !startDate.isEmpty()) && (endDate != null && !endDate.isEmpty())) {
            LocalDate start = LocalDate.parse(startDate);   // "2023-09-28"
            LocalDate end   = LocalDate.parse(endDate);     // "2023-10-27"

            OffsetDateTime startDateTime = start.atStartOfDay().atOffset(ZoneOffset.UTC);

            OffsetDateTime endDateTime = end
                    .plusDays(1)                       // make endDate inclusive
                    .atStartOfDay()
                    .atOffset(ZoneOffset.UTC);
            entities = repository.findByUserIdAndStatusAndCreatedAtBetween(userId, 1, startDateTime, endDateTime, pageable);
        }
        return (entities == null || entities.isEmpty()) ? null : entities.map(mapper::buildDto);
    }

    @Override
    public CreateOrderDto update(CreateOrderDto dto) {
        CreateOrderEntity entity;

        if (dto.getId() == null) { // New item → insert
            entity = mapper.buildEntity(dto);
        } else { // Existing item → update
            entity = repository.findByIdAndStatus(dto.getId(), 1)
                    .orElseThrow(() -> new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Order not found with id: " + dto.getId()));
            mapper.updateEntityFromDto(dto, entity);
        }

        // Save and return DTO
        CreateOrderEntity saved = repository.save(entity);
        return mapper.buildDto(saved);
    }

}

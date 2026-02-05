package com.coffee.service.impl;

import com.coffee.dto.PaymentLinkDto;
import com.coffee.entity.PaymentLinkEntity;
import com.coffee.entityMapper.PaymentLinkEntityMapper;
import com.coffee.exception.GenericException;
import com.coffee.repository.PaymentLinkRepository;
import com.coffee.service.PaymentLinkService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentLinkServiceImpl implements PaymentLinkService {

    private final PaymentLinkEntityMapper mapper;
    private final PaymentLinkRepository repository;

    @Override
    public PaymentLinkDto save(PaymentLinkDto paymentLink) {
        PaymentLinkEntity entity = mapper.buildEntity(paymentLink);
        entity = repository.save(entity);
        return mapper.buildDto(entity);
    }

    @Override
    public PaymentLinkDto findByReferenceId(String referenceId) {
        PaymentLinkEntity entity = repository.findByReferenceIdAndStatus(referenceId, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public PaymentLinkDto update(PaymentLinkDto dto) {
        PaymentLinkEntity entity;

        if (dto.getId() == null) { // New item → insert
            entity = mapper.buildEntity(dto);
        } else { // Existing item → update
            entity = repository.findByIdAndStatus(dto.getId(), 1)
                    .orElseThrow(() -> new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Cart item not found with id: " + dto.getId()));
            mapper.updateEntityFromDto(dto, entity);
        }

        // Save and return DTO
        PaymentLinkEntity saved = repository.save(entity);
        return mapper.buildDto(saved);
    }
}

package com.coffee.service.impl;

import com.coffee.dto.PersistCartDto;
import com.coffee.entity.PersistCartEntity;
import com.coffee.entityMapper.PersistCartEntityMapper;
import com.coffee.exception.GenericException;
import com.coffee.repository.PersistCartRepository;
import com.coffee.service.PersistCartService;
import com.coffee.util.ExceptionConstant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersistCartServiceImpl implements PersistCartService {

    private final PersistCartRepository repository;
    private final PersistCartEntityMapper mapper;

    @Override
    public List<PersistCartDto> findByUserId(String userId) {
        List<PersistCartEntity> persistCartEntities = repository.findByUserIdAndStatus(userId, 1);
        return persistCartEntities.isEmpty() ? Collections.emptyList() : mapper.buildDtos(persistCartEntities);
    }

    @Override
    @Transactional
    public PersistCartDto save(PersistCartDto dto) {
        PersistCartEntity entity;

        if (dto.getId() == null) {
            // New item → insert
            entity = mapper.buildEntity(dto);
        } else {
            // Existing item → update
            entity = repository.findByIdAndStatus(dto.getId(), 1)
                    .orElseThrow(() -> new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Cart item not found with id: " + dto.getId()));
            mapper.updateEntityFromDto(dto, entity);
        }

        // Save and return DTO
        PersistCartEntity saved = repository.save(entity);
        return mapper.buildDto(saved);
    }

    @Override
    @Transactional
    public List<PersistCartDto> saveAll(List<PersistCartDto> dtos) {

        List<Long> ids = dtos.stream()
                .map(PersistCartDto::getId)
                .filter(Objects::nonNull)
                .toList();

        // fetch existing entities in one query
        Map<Long, PersistCartEntity> existingEntities = repository.findAllById(ids).stream()
                .collect(Collectors.toMap(PersistCartEntity::getId, e -> e));

        List<PersistCartEntity> entitiesToSave = new ArrayList<>();

        for (PersistCartDto dto : dtos) {
            if (dto.getId() == null) {
                // new item → map from DTO
                entitiesToSave.add(mapper.buildEntity(dto));
            } else {
                // existing item → update entity
                PersistCartEntity entity = existingEntities.get(dto.getId());
                if (entity != null) {
                    mapper.updateEntityFromDto(dto, entity);
                    entitiesToSave.add(entity);
                }
            }
        }

        // bulk save
        List<PersistCartEntity> saved = repository.saveAll(entitiesToSave);

        // return back as DTOs
        return mapper.buildDtos(saved);
    }



}

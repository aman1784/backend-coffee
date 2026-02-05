package com.coffee.service.impl;

import com.coffee.dto.ShareCartLinkItemDto;
import com.coffee.entity.ShareCartLinkItemEntity;
import com.coffee.entityMapper.ShareCartLinkItemEntityMapper;
import com.coffee.repository.ShareCartLinkItemRepository;
import com.coffee.service.ShareCartLinkItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Aman Kumar Seth
 * @since 27-01-2026
 */

@Service
@RequiredArgsConstructor
public class ShareCartLinkItemServiceImpl implements ShareCartLinkItemService {

    private final ShareCartLinkItemRepository repository;
    private final ShareCartLinkItemEntityMapper mapper;


    @Override
    @Transactional
    public List<ShareCartLinkItemDto> saveAll(List<ShareCartLinkItemDto> dtos) {

        List<Long> ids = dtos.stream()
                .map(ShareCartLinkItemDto::getId)
                .filter(Objects::nonNull)
                .toList();

        // fetch existing entities in one query
        Map<Long, ShareCartLinkItemEntity> existingEntities = repository.findAllById(ids).stream()
                .collect(Collectors.toMap(ShareCartLinkItemEntity::getId, e -> e));

        List<ShareCartLinkItemEntity> entitiesToSave = new ArrayList<>();

        for (ShareCartLinkItemDto dto : dtos) {
            if (dto.getId() == null) {
                // new item → map from DTO
                entitiesToSave.add(mapper.buildEntity(dto));
            } else {
                // existing item → update entity
                ShareCartLinkItemEntity entity = existingEntities.get(dto.getId());
                if (entity != null) {
                    mapper.updateEntityFromDto(dto, entity);
                    entitiesToSave.add(entity);
                }
            }
        }

        // bulk save
        List<ShareCartLinkItemEntity> saved = repository.saveAll(entitiesToSave);

        // return back as DTOs
        return mapper.buildDtos(saved);


    }

    @Override
    public List<ShareCartLinkItemDto> findAllByShareCartLinkId(Long userId) {
        List<ShareCartLinkItemEntity> entities = repository.findAllByShareCartLinkIdAndStatus(userId, 1);
        return entities.isEmpty()? Collections.emptyList(): mapper.buildDtos(entities);
    }
}

package com.coffee.service.impl;

import com.coffee.dto.ShareCartLinkDto;
import com.coffee.entity.ShareCartLinkEntity;
import com.coffee.entityMapper.ShareCartLinkEntityMapper;
import com.coffee.repository.ShareCartLinkRepository;
import com.coffee.service.ShareCartLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@Service
@RequiredArgsConstructor
public class ShareCartLinkServiceImpl implements ShareCartLinkService {

    private final ShareCartLinkRepository repository;
    private final ShareCartLinkEntityMapper mapper;

    @Override
    public ShareCartLinkDto save(ShareCartLinkDto shareCartLinkDto) {
        return mapper.buildDto(repository.save(mapper.buildEntity(shareCartLinkDto)));
    }

    @Override
    public ShareCartLinkDto findByShareCartLink(String shareLink) {
        ShareCartLinkEntity entity = repository.findByShareCartLinkAndStatus(shareLink, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }
}

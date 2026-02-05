package com.coffee.service.impl;

import com.coffee.dto.ConstantsDto;
import com.coffee.entity.ConstantsEntity;
import com.coffee.entityMapper.ConstantsEntityMapper;
import com.coffee.repository.ConstantsRepository;
import com.coffee.service.ConstantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConstantsServiceImpl implements ConstantsService {

    private final ConstantsRepository repository;
    private final ConstantsEntityMapper mapper;

    @Override
    public ConstantsDto findByKey(String key) {
        ConstantsEntity entity = repository.findByKeyAndStatus(key, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }
}

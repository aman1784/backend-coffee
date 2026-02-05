package com.coffee.service.impl;

import com.coffee.dto.UserFavoriteProductDto;
import com.coffee.entity.UserFavoriteProductEntity;
import com.coffee.entityMapper.UserFavoriteProductEntityMapper;
import com.coffee.repository.UserFavoriteProductRepository;
import com.coffee.service.UserFavoriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFavoriteProductServiceImpl implements UserFavoriteProductService {

    private final UserFavoriteProductEntityMapper mapper;
    private final UserFavoriteProductRepository repository;

    @Override
    public UserFavoriteProductDto save(UserFavoriteProductDto dto) {
        return mapper.buildDto(repository.save(mapper.buildEntity(dto)));
    }

    @Override
    public List<UserFavoriteProductDto> findAllByUserId(String userId) {
        return mapper.buildDtos(repository.findAllByUserIdAndStatus(userId, 1));
    }

    @Override
    public UserFavoriteProductDto findByUserIdAndMenuItemKeyAndVariantKey(String userId, String menuItemKey, String variantKey) {
        UserFavoriteProductEntity entity = repository.findByUserIdAndMenuItemKeyAndVariantKeyAndStatus(userId, menuItemKey, variantKey, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public boolean delete(UserFavoriteProductDto dto) {
        repository.deleteById(dto.getId());
        return true;
    }

}

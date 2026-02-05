package com.coffee.service.impl;

import com.coffee.dto.UsersPasswordDto;
import com.coffee.entity.UsersPasswordEntity;
import com.coffee.entityMapper.UsersPasswordEntityMapper;
import com.coffee.repository.UsersPasswordRepository;
import com.coffee.service.UsersPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersPasswordServiceImpl implements UsersPasswordService {

    private final UsersPasswordRepository repository;
    private final UsersPasswordEntityMapper mapper;

    @Override
    public UsersPasswordDto save(UsersPasswordDto usersPasswordDto) {
        UsersPasswordEntity entity = mapper.buildEntity(usersPasswordDto);
        entity = repository.save(entity);
        return mapper.buildDto(entity);
    }

    @Override
    public UsersPasswordDto findByEmailIdOrPhoneNumber(String emailIdOrPhoneNumber) {
        UsersPasswordEntity entity = repository.findByEmailIdOrPhoneNumberAndStatus(emailIdOrPhoneNumber, emailIdOrPhoneNumber, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public UsersPasswordDto findByEmailId(String emailId) {
        UsersPasswordEntity entity = repository.findByEmailIdAndStatus(emailId, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public UsersPasswordDto findByPhoneNumber(String phoneNumber) {
        UsersPasswordEntity entity = repository.findByPhoneNumberAndStatus(phoneNumber, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public UsersPasswordDto update(UsersPasswordDto usersPasswordDto) {
        UsersPasswordEntity entity;

        if (usersPasswordDto.getId() == null) {
            // New item → insert
            entity = mapper.buildEntity(usersPasswordDto);
        }
        else {
            // Existing item → update
            entity = repository.findById(usersPasswordDto.getId())
                    .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + usersPasswordDto.getId()));
            mapper.updateEntityFromDto(usersPasswordDto, entity);
        }

        UsersPasswordEntity savedEntity = repository.save(entity);
        return mapper.buildDto(savedEntity);
    }

    @Override
    public UsersPasswordDto findByUserKey(String userKey) {
        UsersPasswordEntity entity = repository.findByUserKeyAndStatus(userKey, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }
}

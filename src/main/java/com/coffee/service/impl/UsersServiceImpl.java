package com.coffee.service.impl;

import com.coffee.dto.UsersDto;
import com.coffee.entity.UsersEntity;
import com.coffee.entityMapper.UsersEntityMapper;
import com.coffee.exception.GenericException;
import com.coffee.projection.CheckUserEmailOrMobileExistsProjection;
import com.coffee.projection.CheckUserIdProjection;
import com.coffee.projection.CheckUserProjection;
import com.coffee.repository.UsersRepository;
import com.coffee.request.UserSignUpRequest;
import com.coffee.service.UsersService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UsersRepository repository;
    private final UsersEntityMapper mapper;


    @Override
    public List<UsersDto> findIfAnyDataAlreadyExists(UserSignUpRequest request) {
        List<UsersEntity> entities = repository.findIfAnyDataAlreadyExists(request.getEmailId(), request.getPhone());
        return entities.isEmpty() ? null : mapper.buildDtos(entities);
    }

    @Override
    public UsersDto save(UsersDto usersDto) {
        UsersEntity entity = mapper.buildEntity(usersDto);
        entity = repository.save(entity);
        return mapper.buildDto(entity);
    }

    @Override
    public UsersDto findByEmailIdOrPhoneNumber(String emailIdOrPhoneNumber) {
        UsersEntity entity = repository.findByEmailIdOrPhoneNumberAndStatus(emailIdOrPhoneNumber, emailIdOrPhoneNumber, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public CheckUserEmailOrMobileExistsProjection findProjectionByEmailIdOrPhoneNumber(String emailIdOrPhoneNumber) {
        return repository.findProjectionByEmailIdOrPhoneNumberAndStatus(emailIdOrPhoneNumber, emailIdOrPhoneNumber, 1);
    }

    @Override
    public UsersDto findByUserIdAndFullNameAndPhoneNumber(String userId, String customerName, String customerPhoneNumber) {
        UsersEntity entity = repository.findByUserIdAndFullNameAndPhoneNumberAndStatus(userId, customerName, customerPhoneNumber, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public CheckUserIdProjection findByUserId(String userId) {
        return repository.findByUserIdAndStatus(userId, 1);
    }

    @Override
    public UsersDto findByUserKey(String userKey) {
        UsersEntity entity = repository.findByUserKeyAndStatus(userKey, 1);
        return entity == null ? null : mapper.buildDto(entity);
    }

    @Override
    public UsersDto update(UsersDto dto) {
        UsersEntity entity;

        if (dto.getId() == null){
            entity = mapper.buildEntity(dto);
        } else {
            entity = repository.findByIdAndStatus(dto.getId(), 1)
                    .orElseThrow(() -> new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found with id: " + dto.getId()));

            mapper.updateEntityFromDto(dto, entity);
        }

        // Save and return DTO
        UsersEntity saved = repository.save(entity);
        return mapper.buildDto(saved);
    }

    @Override
    public CheckUserProjection findUserKey(String userKey) {
        return repository.findProjectionByUserKeyAndStatus(userKey, 1);
    }
}

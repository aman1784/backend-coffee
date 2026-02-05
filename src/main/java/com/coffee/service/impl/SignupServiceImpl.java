package com.coffee.service.impl;

import com.coffee.dto.UsersDto;
import com.coffee.entity.UsersEntity;
import com.coffee.entityMapper.UsersEntityMapper;
import com.coffee.projection.CheckUserNameProjection;
import com.coffee.repository.UsersRepository;
import com.coffee.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupServiceImpl implements SignupService {

    private final UsersRepository repository;
    private final UsersEntityMapper mapper;

    // METHOD 1 -> USING PROJECTION
    @Override
    public CheckUserNameProjection findByUserName(String userName) {
        return repository.findByUserNameAndStatus(userName, 1);
    }

    // METHOD 2 -> USING ENTITY
//    @Override
//    public UsersDto findByUserNamee(String userName) {
//        UsersEntity entity = repository.findByUserName(userName);
//        return entity == null ? null : mapper.buildDto(entity);
//    }

}

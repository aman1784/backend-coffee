package com.coffee.service;

import com.coffee.dto.UsersDto;
import com.coffee.projection.CheckUserNameProjection;

public interface SignupService {

    // METHOD 1 -> USING PROJECTION
    CheckUserNameProjection findByUserName(String userName);

    // METHOD 2 -> USING DTO
//    UsersDto findByUserNamee(String userName);
}

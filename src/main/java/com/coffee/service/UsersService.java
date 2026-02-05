package com.coffee.service;

import com.coffee.dto.UsersDto;
import com.coffee.projection.CheckUserEmailOrMobileExistsProjection;
import com.coffee.projection.CheckUserIdProjection;
import com.coffee.projection.CheckUserProjection;
import com.coffee.request.UserSignUpRequest;

import java.util.List;

public interface UsersService {

    List<UsersDto> findIfAnyDataAlreadyExists(UserSignUpRequest request);

    UsersDto save(UsersDto usersDto);

    UsersDto findByEmailIdOrPhoneNumber(String emailIdOrPhoneNumber);

    CheckUserEmailOrMobileExistsProjection findProjectionByEmailIdOrPhoneNumber(String emailIdOrPhoneNumber);

    UsersDto findByUserIdAndFullNameAndPhoneNumber(String userId, String customerName, String customerPhoneNumber);

    CheckUserIdProjection findByUserId(String userId);

    UsersDto findByUserKey(String userKey);

    UsersDto update(UsersDto usersDto);

    CheckUserProjection findUserKey(String userKey);
}

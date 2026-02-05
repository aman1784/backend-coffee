package com.coffee.service;

import com.coffee.dto.UsersPasswordDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface UsersPasswordService {

    UsersPasswordDto save(UsersPasswordDto usersPasswordDto);

    UsersPasswordDto findByEmailIdOrPhoneNumber(String emailIdOrPhoneNumber);

    UsersPasswordDto findByEmailId(String emailId);

    UsersPasswordDto findByPhoneNumber(String phoneNumber);

    UsersPasswordDto update(UsersPasswordDto usersPasswordDto);

    UsersPasswordDto findByUserKey(String userKey);
}

package com.coffee.responseMapper;

import com.coffee.dto.UsersDto;
import com.coffee.dto.UsersPasswordDto;
import com.coffee.response.UserLoginResponse;
import com.coffee.response.UserSignUpResponse;
import org.springframework.stereotype.Component;

@Component
public class UserLoginSignUpResponseMapper {


    public UserSignUpResponse mapToUserSignUpResponse(UsersDto usersDto, UsersPasswordDto usersPasswordDto) {

        return UserSignUpResponse.builder()
                .userKey(usersDto.getUserKey())
                .userId(usersDto.getUserId())
                .userName(usersDto.getUserName())
                .firstName(usersDto.getFirstName())
                .middleName(usersDto.getMiddleName())
                .lastName(usersDto.getLastName())
                .fullName(usersDto.getFullName())
                .emailId(usersPasswordDto.getEmailId())
                .phoneNumber(usersPasswordDto.getPhoneNumber())
                .countryCode(usersDto.getCountryCode())
                .userType(usersDto.getUserType())
                .accountCreationMessage("Account created successfully")
                .createdAt(usersDto.getCreatedAt())
                .build();
    }

    public UserLoginResponse mapToUserLoginResponse(UsersDto usersDto) {

        return UserLoginResponse.builder()
                .accessToken("")
                .refreshToken("")
                .tokenType("Bearer")
                .expiresIn(0L)
                .userKey(usersDto.getUserKey())
                .userId(usersDto.getUserId())
                .userName(usersDto.getUserName())
                .firstName(usersDto.getFirstName())
                .middleName(usersDto.getMiddleName())
                .lastName(usersDto.getLastName())
                .fullName(usersDto.getFullName())
                .emailId(usersDto.getEmailId())
                .phoneNumber(usersDto.getPhoneNumber())
                .userType(usersDto.getUserType())
                .profilePictureUrl(usersDto.getProfilePictureUrl())
                .build();
    }

}

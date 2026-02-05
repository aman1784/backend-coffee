package com.coffee.responseMapper;

import com.coffee.dto.UsersDto;
import com.coffee.request.UserSignUpRequest;
import com.coffee.util.UsersUtility;
import com.coffee.util.Utility;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.OffsetDateTime;

@Component
public class UsersResponseMapper {


    public UsersDto mapToSignUpUserDto(@Valid UserSignUpRequest request) {

        return UsersDto.builder()
                .id(null)
                .userKey(Utility.generateMd5Hash(request.getUserName()+request.getPhone()+request.getPassword()+request.getEmailId()))
                .userId(UsersUtility.generateUserId(request.getPhone()))
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .fullName(UsersUtility.generateFullName(request.getFirstName(), request.getMiddleName(), request.getLastName()))
                .userName(request.getUserName())
                .countryCode("+91")
                .phoneNumber(request.getPhone())
                .emailId(request.getEmailId())
                .userType("Customer")
                .dateOfBirth(request.getDateOfBirth())
                .status(1)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

}

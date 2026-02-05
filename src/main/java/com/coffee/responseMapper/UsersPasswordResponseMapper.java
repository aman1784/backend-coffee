package com.coffee.responseMapper;

import com.coffee.dto.UsersDto;
import com.coffee.dto.UsersPasswordDto;
import com.coffee.request.UserSignUpRequest;
import com.coffee.util.DateTimeConverterUtil;
import com.coffee.util.UsersUtility;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.OffsetDateTime;

@Component
public class UsersPasswordResponseMapper {

    public UsersPasswordDto mapToUsersPasswordDto(@Valid UserSignUpRequest request, UsersDto usersDto) {

        return UsersPasswordDto.builder()
                .id(null)
                .password(UsersUtility.encryptPassword(request.getPassword()))
                .emailId(request.getEmailId())
                .phoneNumber(request.getPhone())
                .userKey(usersDto.getUserKey())
                .userId(usersDto.getUserId())
                .userName(usersDto.getUserName())
                .status(1)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}

package com.coffee.usecase.impl;

import com.coffee.dto.UsersDto;
import com.coffee.dto.UsersPasswordDto;
import com.coffee.enums.USER_ROLES;
import com.coffee.exception.GenericException;
import com.coffee.projection.CheckUserNameProjection;
import com.coffee.request.UserLoginRequest;
import com.coffee.request.UserSignUpRequest;
import com.coffee.response.*;
import com.coffee.responseMapper.UserLoginSignUpResponseMapper;
import com.coffee.responseMapper.UsersPasswordResponseMapper;
import com.coffee.responseMapper.UsersResponseMapper;
import com.coffee.service.SignupService;
import com.coffee.service.UsersPasswordService;
import com.coffee.service.UsersService;
import com.coffee.usecase.LoginSignupUseCaseService;
import com.coffee.util.ExceptionConstant;
import com.coffee.util.UsersUtility;
import com.google.common.hash.BloomFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginSignupUseCaseServiceImpl implements LoginSignupUseCaseService {

    private final SignupService signupService;
    private final UsersService usersService;
    private final UsersPasswordService usersPasswordService;
    private final BloomFilter<String> usernameBloomFilter;

    private final UsersResponseMapper usersResponseMapper;
    private final UsersPasswordResponseMapper usersPasswordResponseMapper;
    private final UserLoginSignUpResponseMapper userLoginSignUpResponseMapper;

    @Override
    public BaseResponse<CheckUserNameResponse> checkUserNameExists(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Username is required to check availability");
        }
        if (userName.length() < 4) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Username must be at least 4 characters");
        }
        // METHOD 1 -> USING PROJECTION
        // First-level check using Bloom filter
        if (!usernameBloomFilter.mightContain(userName)){
            log.debug("[SignupUseCaseServiceImpl][checkUserName] userName: {} not present in bloom filter", userName);
            CheckUserNameResponse response = new CheckUserNameResponse(userName, false);
            log.debug("[SignupUseCaseServiceImpl][checkUserName] response: {}", response);
            return BaseResponse.<CheckUserNameResponse>builder().data(response).build();
        }
        log.debug("[SignupUseCaseServiceImpl][checkUserName] userName: {} present in bloom filter", userName);
        CheckUserNameProjection userNamePresent = signupService.findByUserName(userName);
        log.debug("[SignupUseCaseServiceImpl][checkUserName] userNamePresent: {}", userNamePresent);
        if (userNamePresent == null || userNamePresent.getUserName() == null || userNamePresent.getUserName().isEmpty()) {
            CheckUserNameResponse response = new CheckUserNameResponse(userName, false);
            return BaseResponse.<CheckUserNameResponse>builder().data(response).build();
        } else {
            CheckUserNameResponse response = new CheckUserNameResponse(userName, true);
            return BaseResponse.<CheckUserNameResponse>builder().data(response).build();
        }

        // METHOD 2 -> USING DTO
//            UsersDto userNamePresent = signupService.findByUserNamee(userName);
//            log.debug("[SignupUseCaseServiceImpl][checkUserName] userNamePresent: {}", userNamePresent);
//            if (userNamePresent == null || userNamePresent.getUserName() == null || userNamePresent.getUserName().isEmpty()) {
//                CheckUserNameResponse response = new CheckUserNameResponse(userName, false);
//                return BaseResponse.<CheckUserNameResponse>builder().data(response).build();
//            } else {
//                CheckUserNameResponse response = new CheckUserNameResponse(userName, true);
//                return BaseResponse.<CheckUserNameResponse>builder().data(response).build();
//            }
    }

    @Override
    public BaseResponse<UserSignUpResponse> userSignUp(@RequestBody @Valid UserSignUpRequest request) {
        List<UsersDto> usersDtoList = usersService.findIfAnyDataAlreadyExists(request);
        if (usersDtoList != null) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", "Data already exists", "Either email or mobile number already exists");
        }
        validator(request);
        UsersDto usersDto = usersResponseMapper.mapToSignUpUserDto(request);
        usersDto = usersService.save(usersDto);
        log.debug("[SignupUseCaseServiceImpl][userSignUp] usersDto: {}", usersDto);
        UsersPasswordDto usersPasswordDto = usersPasswordResponseMapper.mapToUsersPasswordDto(request, usersDto);
        usersPasswordDto = usersPasswordService.save(usersPasswordDto);
        log.debug("[SignupUseCaseServiceImpl][userSignUp] usersPasswordDto: {}", usersPasswordDto);
        UserSignUpResponse response = userLoginSignUpResponseMapper.mapToUserSignUpResponse(usersDto, usersPasswordDto);
        usernameBloomFilter.put(request.getUserName()); // this will add the username to the bloom filter and increase the chance of false positives and reducing the DB hit
        return BaseResponse.<UserSignUpResponse>builder().data(response).build();
    }

    @Override
    public BaseResponse<UserLoginResponse> userLogin(UserLoginRequest request) {
        UsersPasswordDto usersPasswordDto = usersPasswordService.findByEmailIdOrPhoneNumber(request.getEmailIdOrPhoneNumber());
        log.debug("[SignupUseCaseServiceImpl][userLogin] usersPasswordDto: {}", usersPasswordDto);
        if (usersPasswordDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found, email or mobile number not found");
        }
        boolean isValidPassword = UsersUtility.validatePassword(request.getPassword(), usersPasswordDto.getPassword());
        if (!isValidPassword) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Unauthorized", "Password does not match", "Invalid password");
        }
        log.debug("[SignupUseCaseServiceImpl][userLogin] password found");
        UsersDto usersDto = usersService.findByEmailIdOrPhoneNumber(request.getEmailIdOrPhoneNumber());
        log.debug("[SignupUseCaseServiceImpl][userLogin] usersDto: {}", usersDto);
        USER_ROLES roleType = USER_ROLES.valueOf(usersDto.getUserType().toUpperCase());
        switch (roleType) {
            case CUSTOMER -> accessCustomerRole(roleType.toString());
            case STAFF -> accessStaffRole(roleType.toString());
            case MANAGER -> accessManagerRole(roleType.toString());
        }
        UserLoginResponse response = userLoginSignUpResponseMapper.mapToUserLoginResponse(usersDto);
        return BaseResponse.<UserLoginResponse>builder().data(response).build();
//        throw new GenericException(ExceptionConstant.badRequestCode, "Unauthorized", "Unauthorized", "Unauthorized");
    }


    private void accessManagerRole(String roleType) {
        if (roleType.equals(USER_ROLES.MANAGER.getValue())) {

        }
    }

    private void accessStaffRole(String roleType) {
        if (roleType.equals(USER_ROLES.STAFF.getValue())) {

        }
    }

    private void accessCustomerRole(String roleType) {
        if (roleType.equals(USER_ROLES.CUSTOMER.getValue())) {

        }
    }

    private void validator(@Valid UserSignUpRequest request) {
        if (request.getEmailId() == null || request.getEmailId().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Email is required");
        }
        if (request.getPhone().length() != 10) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Phone number must be 10 digits");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Password and Confirm Password must be same");
        }
    }
}

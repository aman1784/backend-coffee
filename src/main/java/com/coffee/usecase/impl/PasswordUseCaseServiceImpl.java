package com.coffee.usecase.impl;

import com.coffee.dto.UsersPasswordDto;
import com.coffee.enums.CHANGE_PASSWORD_TYPE;
import com.coffee.exception.GenericException;
import com.coffee.projection.CheckUserEmailOrMobileExistsProjection;
import com.coffee.request.ResetPasswordRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.ResetPasswordResponse;
import com.coffee.responseMapper.PasswordResponseMapper;
import com.coffee.service.UsersPasswordService;
import com.coffee.service.UsersService;
import com.coffee.usecase.PasswordUseCaseService;
import com.coffee.util.ExceptionConstant;
import com.coffee.util.UsersUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordUseCaseServiceImpl implements PasswordUseCaseService {

    private final UsersService usersService;
    private final UsersPasswordService usersPasswordService;

    private final PasswordResponseMapper passwordResponseMapper;


    @Override
    public BaseResponse<String> checkEmailOrMobileExists(String emailOrMobile) {
        if (emailOrMobile == null || emailOrMobile.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Email or phone number is required");
        }

        CheckUserEmailOrMobileExistsProjection checkUserEmailOrMobileExistsProjection = usersService.findProjectionByEmailIdOrPhoneNumber(emailOrMobile);
        log.info("checkEmailOrMobileExistsProjection {}", checkUserEmailOrMobileExistsProjection);
        if (checkUserEmailOrMobileExistsProjection == null || checkUserEmailOrMobileExistsProjection.getEmailId() == null || checkUserEmailOrMobileExistsProjection.getPhoneNumber() == null){
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Email or phone number is not registered");
        }
        return BaseResponse.<String>builder().data("exists").build();
    }

    @Override
    public BaseResponse<ResetPasswordResponse> resetPassword(ResetPasswordRequest request) {
        if ((request.getEmailId() == null || request.getEmailId().isEmpty()) && (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty())){
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Email or phone number is required");
        }
        UsersPasswordDto usersPasswordDto = null;
        if (request.getEmailId() != null && !request.getEmailId().isEmpty()) {
            usersPasswordDto = usersPasswordService.findByEmailId(request.getEmailId());
            if (usersPasswordDto == null) {
                throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Email is not registered");
            }
        }
        else {
            usersPasswordDto = usersPasswordService.findByPhoneNumber(request.getPhoneNumber());
            if (usersPasswordDto == null) {
                throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Phone number is not registered");
            }
        }

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "New password is required");
        }

        // forgot password
        if (request.getChangePasswordType().equalsIgnoreCase(CHANGE_PASSWORD_TYPE.FORGOT_PASSWORD.getLabel())){
            usersPasswordDto.setPassword(UsersUtility.encryptPassword(request.getNewPassword()));
            usersPasswordDto.setUpdatedAt(OffsetDateTime.now());
            usersPasswordDto = usersPasswordService.update(usersPasswordDto);
            ResetPasswordResponse response = passwordResponseMapper.mapToResetPasswordResponse(usersPasswordDto);
            return BaseResponse.<ResetPasswordResponse>builder().data(response).build();
        }

        // reset password
        else if (request.getChangePasswordType().equalsIgnoreCase(CHANGE_PASSWORD_TYPE.RESET_PASSWORD.getLabel())) {
            if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Old password is required");
            }
            boolean isOldValidPassword = UsersUtility.validatePassword(request.getOldPassword(), usersPasswordDto.getPassword());
            if (!isOldValidPassword) {
                throw new GenericException(ExceptionConstant.badRequestCode, "Unauthorized", "Old Password does not match", "Invalid old password entered");
            }
            log.debug("[SignupUseCaseServiceImpl][userLogin] password found");

            // check new password is the same as old password -> throw error, because it is not allowed
            boolean isNewValidPasswordIsSameAsOldPassword = UsersUtility.validatePassword(request.getNewPassword(), usersPasswordDto.getPassword()); // check new password is the same as old password
            if (isNewValidPasswordIsSameAsOldPassword) {
                throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", "New password is same as old password", "New password is same as old password. Please enter a different password.");
            }

            usersPasswordDto.setPassword(UsersUtility.encryptPassword(request.getNewPassword()));
            usersPasswordDto.setUpdatedAt(OffsetDateTime.now());
            usersPasswordDto = usersPasswordService.update(usersPasswordDto);
            ResetPasswordResponse response = passwordResponseMapper.mapToResetPasswordResponse(usersPasswordDto);
            return BaseResponse.<ResetPasswordResponse>builder().data(response).build();
        }

        throw new GenericException(ExceptionConstant.otherError, "Bad Request", ExceptionConstant.otherErrorMessage, "Something went wrong. Check your request type is correct (either 'forgot password' or 'reset password').");
    }
}

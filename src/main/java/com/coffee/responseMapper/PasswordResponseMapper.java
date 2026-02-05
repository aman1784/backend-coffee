package com.coffee.responseMapper;

import com.coffee.dto.UsersPasswordDto;
import com.coffee.response.ResetPasswordResponse;
import org.springframework.stereotype.Component;

@Component
public class PasswordResponseMapper {
    public ResetPasswordResponse mapToResetPasswordResponse(UsersPasswordDto usersPasswordDto) {
        return ResetPasswordResponse.builder()
//                .passwordResetMessage("Password reset link sent successfully")
                .emailId(usersPasswordDto.getEmailId())
                .phoneNumber(usersPasswordDto.getPhoneNumber())
                // .passwordResetLink("")
                // .passwordResetToken("")
                // .passwordResetTokenExpiry("")
                .password("")
                .build();
    }
}

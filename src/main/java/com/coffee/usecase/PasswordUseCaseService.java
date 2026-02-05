package com.coffee.usecase;

import com.coffee.request.ResetPasswordRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.ResetPasswordResponse;
import jakarta.validation.Valid;

public interface PasswordUseCaseService {
    
    BaseResponse<String> checkEmailOrMobileExists(String emailOrMobile);

    BaseResponse<ResetPasswordResponse> resetPassword(@Valid ResetPasswordRequest request);

}

package com.coffee.usecase;

import com.coffee.request.UserLoginRequest;
import com.coffee.request.UserSignUpRequest;
import com.coffee.response.*;
import jakarta.validation.Valid;

public interface LoginSignupUseCaseService {

    BaseResponse<CheckUserNameResponse> checkUserNameExists(String userName);

    BaseResponse<UserSignUpResponse> userSignUp(@Valid UserSignUpRequest request);

    BaseResponse<UserLoginResponse> userLogin(@Valid UserLoginRequest request);

}

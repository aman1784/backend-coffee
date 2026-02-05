package com.coffee.controller;

import com.coffee.request.UserLoginRequest;
import com.coffee.request.UserSignUpRequest;
import com.coffee.response.*;
import com.coffee.usecase.LoginSignupUseCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/user")
@Slf4j
@RequiredArgsConstructor
public class LoginSignupV1Controller {

    private final LoginSignupUseCaseService signUpUseCaseService;

    @GetMapping(value = "/checkUserNameExists/{userName}")
    public ResponseEntity<BaseResponse<CheckUserNameResponse>> checkUserNameExists(@PathVariable("userName") String userName) {
        log.debug("[UserController][checkUserName] request: {}", userName);
        BaseResponse<CheckUserNameResponse> response = signUpUseCaseService.checkUserNameExists(userName);
        log.debug("[UserController][checkUserName] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<BaseResponse<UserSignUpResponse>> userSignUp(@RequestBody @Valid UserSignUpRequest request) {
        log.debug("[UserController][userSignUp] request: {}", request);
        BaseResponse<UserSignUpResponse> response = signUpUseCaseService.userSignUp(request);
        log.debug("[UserController][userSignUp] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<BaseResponse<UserLoginResponse>> userLogin(@RequestBody @Valid UserLoginRequest request) {
        log.debug("[UserController][userLogin] request: {}", request);
        BaseResponse<UserLoginResponse> response = signUpUseCaseService.userLogin(request);
        log.debug("[UserController][userLogin] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

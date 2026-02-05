package com.coffee.controller;

import com.coffee.request.ResetPasswordRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.ResetPasswordResponse;
import com.coffee.usecase.PasswordUseCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1.0/password")
@RequiredArgsConstructor
@Slf4j
public class PasswordController {

    private final PasswordUseCaseService service;

    /**
     * this api is used to check email or mobile exists
     * when the user forgets the password and wants to reset
     */
    @GetMapping(value = "/checkEmailOrMobileExists/{emailOrMobile}")
    public ResponseEntity<BaseResponse<String>> checkEmailOrMobileExists(@PathVariable("emailOrMobile") String emailOrMobile) {
        log.info("checkEmailOrMobileExists {}", emailOrMobile);
        BaseResponse<String> response = service.checkEmailOrMobileExists(emailOrMobile);
        log.info("checkEmailOrMobileExists response {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * This method is used to reset password
     * when the user forgets the password and wants to reset.
     * The same api will be used to update the password when the user is logged in and wants to change the password
     */
    @PatchMapping(value = "/resetPassword")
    public ResponseEntity<BaseResponse<ResetPasswordResponse>> resetPassword(@RequestBody ResetPasswordRequest request) {
        log.debug("[UserController][forgotPassword] request: {}", request);
        BaseResponse<ResetPasswordResponse> response = service.resetPassword(request);
        log.debug("[UserController][forgotPassword] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

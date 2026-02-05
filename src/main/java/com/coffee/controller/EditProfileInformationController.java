package com.coffee.controller;

import com.coffee.request.EditProfileInformationRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.EditProfileInformationResponse;
import com.coffee.usecase.EditProfileInformationUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aman Kumar Seth
 */

@RestController
@RequestMapping(path = "/api/v1.0/edit")
@RequiredArgsConstructor
@Slf4j
public class EditProfileInformationController {

    private final EditProfileInformationUseCaseService service;

    /**
     * Edit Profile
     * @param request - Take EditProfileInformationRequest
     * @return - EditProfileInformationResponse
     */
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<EditProfileInformationResponse>> editProfileInformation(@ModelAttribute EditProfileInformationRequest request) {

        log.debug("[EditProfileInformationController][editProfileInformation] request: {}", request);
        BaseResponse<EditProfileInformationResponse> response = service.editProfileInformation(request);
        log.debug("[EditProfileInformationController][editProfileInformation] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

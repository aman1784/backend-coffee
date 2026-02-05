package com.coffee.controller;

import com.coffee.response.BaseResponse;
import com.coffee.response.FrequentOrderItemsResponse;
import com.coffee.response.GetProfileInformationResponse;
import com.coffee.usecase.ProfileInformationUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aman Kumar Seth
 */

@RestController
@RequestMapping(path = "/api/v1.0/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileInformationController {

    private final ProfileInformationUseCaseService service;

    @GetMapping(value = "/getProfileInformation")
    public ResponseEntity<BaseResponse<GetProfileInformationResponse>> getProfileInformation(@RequestParam(value = "userKey") String userKey) {
        log.debug("[ProfileInformationController][getProfileInformation] request: userKey: {}", userKey);
        BaseResponse<GetProfileInformationResponse> response = service.getProfileInformation(userKey);
        log.debug("[ProfileInformationController][getProfileInformation] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/frequentOrderItems")
    public ResponseEntity<BaseResponse<FrequentOrderItemsResponse>> getFrequentOrderItems(@RequestParam(value = "userKey") String userKey) {
        log.debug("[ProfileInformationController][getFrequentOrderItems] request: userKey: {}", userKey);
        BaseResponse<FrequentOrderItemsResponse> response = service.getFrequentOrderItems(userKey);
        log.debug("[ProfileInformationController][getFrequentOrderItems] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

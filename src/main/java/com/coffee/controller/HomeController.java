package com.coffee.controller;

import com.coffee.response.BaseResponse;
import com.coffee.response.HomePageDataResponse;
import com.coffee.usecase.HomePageUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1.0/home")
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final HomePageUseCaseService service;

    @GetMapping(value = "/pageData")
    public ResponseEntity<BaseResponse<HomePageDataResponse>> pageData() {
        log.debug("[HomeController][pageData] request");
        BaseResponse<HomePageDataResponse> response = service.pageData();
        log.debug("[HomeController][pageData] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

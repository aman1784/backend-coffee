package com.coffee.usecase.impl;

import com.coffee.dto.ConstantsDto;
import com.coffee.response.BaseResponse;
import com.coffee.response.ConstantsDataResponse;
import com.coffee.response.HomePageDataResponse;
import com.coffee.service.ConstantsService;
import com.coffee.usecase.HomePageUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomePageUseCaseServiceImpl implements HomePageUseCaseService {

    private final ConstantsService constantsService;

    @Cacheable(value = "homePageData")
    @Override
    public BaseResponse<HomePageDataResponse> pageData() {

        ConstantsDto bannerTitleDto = constantsService.findByKey("home_page_banner_title");
        ConstantsDto bannerSubtitleDto = constantsService.findByKey("home_page_banner_subtitle");

        ConstantsDataResponse titleResponse = ConstantsDataResponse.builder().key(bannerTitleDto.getKey()).value(bannerTitleDto.getValue()).build();
        ConstantsDataResponse subtitleResponse = ConstantsDataResponse.builder().key(bannerSubtitleDto.getKey()).value(bannerSubtitleDto.getValue()).build();

        HomePageDataResponse response = new HomePageDataResponse();
        response.setConstants(List.of(titleResponse, subtitleResponse));

        return BaseResponse.<HomePageDataResponse>builder().data(response).build();
    }
}

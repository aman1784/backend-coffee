package com.coffee.usecase;

import com.coffee.response.BaseResponse;
import com.coffee.response.HomePageDataResponse;

public interface HomePageUseCaseService {

    BaseResponse<HomePageDataResponse> pageData();
}

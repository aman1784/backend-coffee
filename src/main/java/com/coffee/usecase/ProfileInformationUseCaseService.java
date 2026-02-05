package com.coffee.usecase;

import com.coffee.response.BaseResponse;
import com.coffee.response.FrequentOrderItemsResponse;
import com.coffee.response.GetProfileInformationResponse;

/**
 * @author Aman Kumar Seth
 */

public interface ProfileInformationUseCaseService {

    BaseResponse<GetProfileInformationResponse> getProfileInformation(String userKey);

    BaseResponse<FrequentOrderItemsResponse> getFrequentOrderItems(String userKey);
}

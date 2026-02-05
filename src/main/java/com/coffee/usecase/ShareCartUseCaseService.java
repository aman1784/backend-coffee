package com.coffee.usecase;

import com.coffee.request.CreateShareCartLinkRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.CreateShareCartLinkResponse;
import com.coffee.response.ShareCartDetailsResponse;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

public interface ShareCartUseCaseService {

    BaseResponse<CreateShareCartLinkResponse> createShareCartLink(CreateShareCartLinkRequest request);

    BaseResponse<ShareCartDetailsResponse> getShareCartInformation(String shareLink);
}

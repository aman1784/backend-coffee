package com.coffee.usecase;

import com.coffee.request.EditProfileInformationRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.EditProfileInformationResponse;

public interface EditProfileInformationUseCaseService {

    BaseResponse<EditProfileInformationResponse> editProfileInformation(EditProfileInformationRequest request);
}

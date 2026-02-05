package com.coffee.usecase;

import com.coffee.request.PersistCartRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.GetCartResponse;
import com.coffee.response.PersistCartResponse;
import jakarta.validation.Valid;

public interface PersistCartUseCaseService {
    
    BaseResponse<PersistCartResponse> persistCart(@Valid PersistCartRequest request);

    BaseResponse<GetCartResponse> getCartDetail(String userId);
}

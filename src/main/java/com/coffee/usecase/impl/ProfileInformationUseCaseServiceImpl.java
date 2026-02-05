package com.coffee.usecase.impl;

import com.coffee.dto.UsersDto;
import com.coffee.exception.GenericException;
import com.coffee.response.BaseResponse;
import com.coffee.response.FrequentOrderItemsResponse;
import com.coffee.response.GetProfileInformationResponse;
import com.coffee.responseMapper.ProfileInformationResponseMapper;
import com.coffee.service.UsersService;
import com.coffee.usecase.ProfileInformationUseCaseService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Aman Kumar Seth
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileInformationUseCaseServiceImpl implements ProfileInformationUseCaseService {

    private final UsersService usersService;

    private final ProfileInformationResponseMapper profileInformationResponseMapper;


    @Override
    public BaseResponse<GetProfileInformationResponse> getProfileInformation(String userKey) {
        if (userKey == null || userKey.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User key is required");
        }

        UsersDto usersDto = usersService.findByUserKey(userKey);
        log.debug("[ProfileInformationUseCaseServiceImpl][getProfileInformation] usersDto: {}", usersDto);
        if (usersDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found");
        }

        GetProfileInformationResponse response = profileInformationResponseMapper.mapToGetProfileInformationResponse(usersDto);
        return BaseResponse.<GetProfileInformationResponse>builder().data(response).build();
    }

    @Override
    public BaseResponse<FrequentOrderItemsResponse> getFrequentOrderItems(String userKey) {
        return null;
    }
}

package com.coffee.usecase.impl;

import com.coffee.dto.PersistCartDto;
import com.coffee.dto.ShareCartLinkDto;
import com.coffee.dto.ShareCartLinkItemDto;
import com.coffee.exception.GenericException;
import com.coffee.projection.CheckUserIdProjection;
import com.coffee.projection.CheckUserProjection;
import com.coffee.request.CreateShareCartLinkRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.CreateShareCartLinkResponse;
import com.coffee.response.ShareCartDetailsResponse;
import com.coffee.responseMapper.ShareCartLinkResponseMapper;
import com.coffee.service.PersistCartService;
import com.coffee.service.ShareCartLinkItemService;
import com.coffee.service.ShareCartLinkService;
import com.coffee.service.UsersService;
import com.coffee.usecase.ShareCartUseCaseService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ShareCartUseCaseServiceImpl implements ShareCartUseCaseService {

    private final ShareCartLinkService shareCartLinkService;
    private final UsersService usersService;
    private final PersistCartService persistCartService;
    private final ShareCartLinkItemService shareCartLinkItemService;

    private final ShareCartLinkResponseMapper shareCartLinkResponseMapper;

    @Override
    public BaseResponse<CreateShareCartLinkResponse> createShareCartLink(CreateShareCartLinkRequest request) {
        if (request.getUserKey() == null || request.getUserKey().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User key is required");
        }

        CheckUserProjection checkUserProjection = usersService.findUserKey(request.getUserKey());
        log.debug("[ShareCartUseCaseServiceImpl][createShareCartLink] checkUserProjection: {}:", checkUserProjection.toString());
        if (checkUserProjection.getUserKey() == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found");
        }

        ShareCartLinkDto shareCartLinkDto = shareCartLinkResponseMapper.createShareCartLinkDtoFromCreateShareCartLinkRequest(checkUserProjection);
        log.debug("[ShareCartUseCaseServiceImpl][createShareCartLink] shareCartLinkDto mapped: {}", shareCartLinkDto);

        shareCartLinkDto = shareCartLinkService.save(shareCartLinkDto);
        log.debug("[ShareCartUseCaseServiceImpl][createShareCartLink] shareCartLinkDto savedToDB: {}", shareCartLinkDto);

        List<PersistCartDto> persistCartDtoList = persistCartService.findByUserId(shareCartLinkDto.getCreatedByUserId());
        log.debug("[ShareCartUseCaseServiceImpl][createShareCartLink] persistCartDtoList: {}", persistCartDtoList);
        if (persistCartDtoList == null || persistCartDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "No Items found to share in the link");
        }

        List<ShareCartLinkItemDto> shareCartLinkItemDtoList = shareCartLinkResponseMapper.insertIntoShareCartLinkItemDto(shareCartLinkDto, persistCartDtoList);
        log.debug("[ShareCartUseCaseServiceImpl][createShareCartLink] shareCartLinkItemDtoList: {}", shareCartLinkItemDtoList);
        shareCartLinkItemDtoList = shareCartLinkItemService.saveAll(shareCartLinkItemDtoList);
        log.debug("[ShareCartUseCaseServiceImpl][createShareCartLink] shareCartLinkItemDtoList savedToDB: {}", shareCartLinkItemDtoList);

        CreateShareCartLinkResponse response = shareCartLinkResponseMapper.createShareCartLinkResponse(shareCartLinkDto);

        return BaseResponse.<CreateShareCartLinkResponse>builder().data(response).build();
    }

    @Override
    public BaseResponse<ShareCartDetailsResponse> getShareCartInformation(String shareLink) {
        if (shareLink == null || shareLink.isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Share Id required");
        }

        ShareCartLinkDto shareCartLinkDto = shareCartLinkService.findByShareCartLink(shareLink);
        log.debug("[ShareCartUseCaseServiceImpl][getShareCartInformation] shareCartLinkDto: {}", shareCartLinkDto);
        if (shareCartLinkDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "Either wrong share Id or Link expired");
        }

        String userId = shareCartLinkDto.getCreatedByUserId();
        log.debug("[ShareCartUseCaseServiceImpl][getShareCartInformation] userId: {}", userId);

        CheckUserIdProjection checkUserIdProjectionDetails = usersService.findByUserId(userId);
        log.debug("[ShareCartUseCaseServiceImpl][getShareCartInformation] checkUserIdProjectionDetails: {}", checkUserIdProjectionDetails);
        if (checkUserIdProjectionDetails == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found");
        }

        List<ShareCartLinkItemDto> shareCartLinkItemDtoList = shareCartLinkItemService.findAllByShareCartLinkId(shareCartLinkDto.getId());
        log.debug("[ShareCartUseCaseServiceImpl][getShareCartInformation] shareCartLinkItemDtoList: {}", shareCartLinkItemDtoList);
        if (shareCartLinkItemDtoList.isEmpty()) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, ExceptionConstant.notFound, ExceptionConstant.notFound, "No Items in cart");
        }

        ShareCartDetailsResponse response = shareCartLinkResponseMapper.mapToGetShareCartDetailsResponse(shareCartLinkItemDtoList, shareCartLinkDto);
        return BaseResponse.<ShareCartDetailsResponse>builder().data(response).build();

    }
}

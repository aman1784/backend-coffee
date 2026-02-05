package com.coffee.controller;

import com.coffee.request.CreateShareCartLinkRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.CreateShareCartLinkResponse;
import com.coffee.response.ShareCartDetailsResponse;
import com.coffee.usecase.ShareCartUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@RestController
@RequestMapping(path = "/api/v1.0/share")
@RequiredArgsConstructor
@Slf4j
public class ShareCartV1Controller {

    private final ShareCartUseCaseService service;

    @PostMapping(value = "/create-share-link")
    public ResponseEntity<BaseResponse<CreateShareCartLinkResponse>> createShareCartLink(@RequestBody CreateShareCartLinkRequest request){
        log.debug("[ShareCartV1Controller][createShareCartLink] request: {}", request);
        BaseResponse<CreateShareCartLinkResponse> response = service.createShareCartLink(request);
        log.debug("[ShareCartV1Controller][createShareCartLink] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/cart-details")
    public ResponseEntity<BaseResponse<ShareCartDetailsResponse>> getShareCartInformation(@RequestParam(value = "shareId") String shareLink) {
        log.debug("[ShareCartV1Controller][getShareCartInformation] request -> shareId: {}", shareLink);
        BaseResponse<ShareCartDetailsResponse> response = service.getShareCartInformation(shareLink);
        log.debug("[ShareCartV1Controller][getShareCartInformation] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

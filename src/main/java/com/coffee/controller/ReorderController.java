package com.coffee.controller;

import com.coffee.request.ReorderRequest;
import com.coffee.response.BaseResponse;
import com.coffee.usecase.ReOrderUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aman Kumar Seth
 * @version 1.0
 * @since 2025-11-28
 */

@RestController
@RequestMapping(value = "/api/v1.0/reorder")
@RequiredArgsConstructor
@Slf4j
public class ReorderController {

    private final ReOrderUseCaseService reOrderUseCaseService;

    @PostMapping(value = "/item")
    public ResponseEntity<BaseResponse<String>> reorder(@RequestBody ReorderRequest request) {
        log.debug("[ReorderController][reorder] request: {}", request);
        BaseResponse<String> response = reOrderUseCaseService.reorder(request);
        log.debug("[ReorderController][reorder] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

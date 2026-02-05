package com.coffee.controller;

import com.coffee.request.OrderMenuCategoryFilterRequest;
import com.coffee.response.*;
import com.coffee.usecase.OrderMenuUsecaseService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1.0/menu")
@RequiredArgsConstructor
public class OrderMenuV1Controller {

    private final OrderMenuUsecaseService service;

    @GetMapping(value = "/getAllMenuDetailsExcelSheet", produces = "text/csv")
    public void getAllMenuDetailsExcelSheet(HttpServletResponse response) {
        try {
            // Step 1: Generate the CSV file
            // Generate the CSV (service method returns byte[])
            byte[] csvFile = service.getAllMenuDetailsExcelSheet();

            // Step 2: Set download response
            // Set headers so browser forces download
            String fileName = "menu_details.csv";
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentLength(csvFile.length);

            // Write to output stream
            response.getOutputStream().write(csvFile);
            response.flushBuffer();

        } catch (Exception e) {
            throw new RuntimeException("[OrderMenuV1Controller][getAllMenuDetailsExcelSheet] Failed to generate CSV download", e);
        }
    }

    @GetMapping(value = "/featuredProducts")
    public ResponseEntity<BaseResponse<List<FeaturedOrderMenuResponse>>> featuredOrderMenu(){
        log.debug("[OrderMenuController][featuredOrderMenu] request");
        BaseResponse<List<FeaturedOrderMenuResponse>> response = service.featuredOrderMenu();
        log.debug("[OrderMenuController][featuredOrderMenu] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/getAllCategories")
    private ResponseEntity<BaseResponse<List<OrderMenuCategoryResponse>>> listOfCategories(){
        log.debug("[OrderMenuController][listOfCategories] request");
        BaseResponse<List<OrderMenuCategoryResponse>> response = service.listOfCategories();
        log.debug("[OrderMenuController][listOfCategories] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/getAllMenuItems")
    public ResponseEntity<BaseResponse<OrderMenuItemPageResponse>> getAllOrderMenu(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                   @RequestBody OrderMenuCategoryFilterRequest request){
        log.debug("[OrderMenuController][getAllOrderMenu] request: {}", request);
        BaseResponse<OrderMenuItemPageResponse> response = service.getAllOrderMenu(page, size, request);
        log.debug("[OrderMenuController][getAllOrderMenu] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/getMenuItemDetail/{menuItemKey}")
    public ResponseEntity<BaseResponse<OrderMenuItemResponse>> getMenuItemDetail(@PathVariable String menuItemKey){
        log.debug("[OrderMenuController][getMenuItemByKey] request: {}", menuItemKey);
        BaseResponse<OrderMenuItemResponse> response = service.getMenuItemDetail(menuItemKey);
        log.debug("[OrderMenuController][getMenuItemByKey] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // implement debouncing from the front end
    @GetMapping(value = "/get/{searchWord}")
    public ResponseEntity<BaseResponse<OrderMenuSearchResponse>> getSearchWord(@PathVariable String searchWord){
        log.debug("[OrderMenuController][getSearchWord] request: {}", searchWord);
        BaseResponse<OrderMenuSearchResponse> response = service.getSearchWord(searchWord);
        log.debug("[OrderMenuController][getSearchWord] response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // add item -> MANAGER ACCESS ONLY
//    @PostMapping(value = "/add/item")
//    public ResponseEntity<BaseResponse<AddItemInMenuAndCategoryResponse>> addItemInMenuAndCategory(@RequestBody AddItemInMenuAndCategoryRequest request){
//        log.debug("[OrderMenuController][addItemInMenuAndCategory] request: {}", request);
//        BaseResponse<AddItemInMenuAndCategoryResponse> response = service.addItemInMenuAndCategory(request);
//        log.debug("[OrderMenuController][addItemInMenuAndCategory] response: {}", response);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

}

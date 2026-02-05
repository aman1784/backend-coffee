package com.coffee.usecase;

import com.coffee.request.OrderMenuCategoryFilterRequest;
import com.coffee.response.*;

import java.util.List;

public interface OrderMenuUsecaseService {

    byte[] getAllMenuDetailsExcelSheet();

    BaseResponse<List<FeaturedOrderMenuResponse>> featuredOrderMenu();

    BaseResponse<List<OrderMenuCategoryResponse>> listOfCategories();

    BaseResponse<OrderMenuItemPageResponse> getAllOrderMenu(int page, int size, OrderMenuCategoryFilterRequest request);

    BaseResponse<OrderMenuItemResponse> getMenuItemDetail(String menuItemKey);

    BaseResponse<OrderMenuSearchResponse> getSearchWord(String searchWord);

//    BaseResponse<AddItemInMenuAndCategoryResponse> addItemInMenuAndCategory(AddItemInMenuAndCategoryRequest request);

}

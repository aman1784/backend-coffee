package com.coffee.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMenuSearchResponse {

    private List<OrderMenuItemResponse> menus;
    private List<OrderMenuCategoryResponse> categories;
}

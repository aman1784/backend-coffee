package com.coffee.response;

import com.coffee.dto.OrderMenuCategoryDto;
import com.coffee.dto.OrderMenuItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemInMenuAndCategoryResponse {

    private OrderMenuItemDto menuItem;
    private OrderMenuCategoryDto menuCategory;

}


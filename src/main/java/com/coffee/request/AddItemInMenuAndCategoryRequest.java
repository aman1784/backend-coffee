package com.coffee.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemInMenuAndCategoryRequest {

    private AddOrderMenuItem menuItem;
    private AddOrderMenuCategory menuCategory;
}

package com.coffee.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMenuCategoryFilterRequest {

    // Category key may be present -> if present, filter menu items by category key else return all
    private List<String> categoryKey;

}

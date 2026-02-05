package com.coffee.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMenuCategoryResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

//    private Long categoryId;
    private String categoryKey;
    private String categoryName;

}

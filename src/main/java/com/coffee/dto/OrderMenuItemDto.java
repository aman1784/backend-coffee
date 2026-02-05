package com.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMenuItemDto {

    private Long id;
    private String menuItemKey;
    private Long categoryId;
    private String name;
    private String slug;
    private String shortDescription;
//    private BigDecimal mrp;
    private String ingredients;
    private String nutritionInfo;
    private String description;
    private String image;
    private Integer isFeatured;
    private Integer displayOrder;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}

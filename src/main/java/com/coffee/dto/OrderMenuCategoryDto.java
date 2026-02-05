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
public class OrderMenuCategoryDto {

    private Long id;
    private String categoryKey;
    private Long parentId;
    private String name;
    private String slug;
    private String categoryDescription;
    private String metaDescription;
    private String image;
    private Integer displayOrder;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}

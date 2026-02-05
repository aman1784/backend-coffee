package com.coffee.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserFavoriteProductDto {

    private Long id;
    private String userId;
    private String menuItemKey;
    private String variantKey;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}

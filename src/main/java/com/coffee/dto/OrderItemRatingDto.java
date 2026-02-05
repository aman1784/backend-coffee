package com.coffee.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemRatingDto {

    private Long id;
    private String orderId;
    private String menuItemKey;
    private double rating;
    private String comment;
    private String image;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String userName; // createdBy
}

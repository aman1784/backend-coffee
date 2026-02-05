package com.coffee.request;

import lombok.*;

/**
 * @author Aman Kumar Seth
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RateOrderItem {

    private String menuItemKey;
    private String comment;
    private double rating;

}

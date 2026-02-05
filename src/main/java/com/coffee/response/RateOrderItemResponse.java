package com.coffee.response;

import com.coffee.request.RateOrderItem;
import lombok.*;

import java.util.List;

/**
 * @author Aman Kumar Seth
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RateOrderItemResponse {

    private List<RateOrderItem> orderItems;
}

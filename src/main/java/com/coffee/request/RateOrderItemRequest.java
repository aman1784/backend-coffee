package com.coffee.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Aman Kumar Seth
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateOrderItemRequest extends BaseRequest{

    private String orderId;
    private List<RateOrderItem> orderItems;
    private String userName;
}

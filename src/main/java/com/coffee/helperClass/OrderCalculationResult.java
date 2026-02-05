package com.coffee.helperClass;

import com.coffee.dto.CreateOrderDto;
import com.coffee.dto.CreateOrderItemDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCalculationResult {

    private CreateOrderDto order;
    private List<CreateOrderItemDto> items;

}

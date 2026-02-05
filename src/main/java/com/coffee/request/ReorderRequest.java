package com.coffee.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReorderRequest extends BaseRequest{

    @NotNull(message = "Order Id can not be null")
    @NotBlank(message = "Order Id can not be blank")
    private String orderId;

    @NotNull(message = "Order Key can not be null")
    @NotBlank(message = "Order Key can not be blank")
    private String orderKey;

    @NotNull(message = "User Id can not be null")
    @NotBlank(message = "User Id can not be blank")
    private String userId;
}

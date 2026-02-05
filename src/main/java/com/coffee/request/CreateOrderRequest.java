package com.coffee.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest extends BaseRequest {

    @NotNull(message = "User Id can not be null")
    @NotBlank(message = "User Id can not be blank")
    private String userId;

    @NotNull(message = "Customer name can not be null")
    @NotBlank(message = "Customer name can not be blank")
    private String customerName;

    @NotNull(message = "Customer phone number can not be null")
    @NotBlank(message = "Customer phone number can not be blank")
    private String customerPhoneNumber;

    private List<CreateOrderItemRequest> orderItems;

    private String paymentType; // cash or online
}

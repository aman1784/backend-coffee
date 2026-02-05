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
public class PersistCartRequest extends BaseRequest{

    @NotNull(message = "User Id can not be null")
    @NotBlank(message = "User Id can not be blank")
    private String userId;

    private List<CartOrderItemRequest> orderItems;

}

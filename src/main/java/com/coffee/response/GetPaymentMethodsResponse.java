package com.coffee.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPaymentMethodsResponse {

    private String paymentMethod;

}

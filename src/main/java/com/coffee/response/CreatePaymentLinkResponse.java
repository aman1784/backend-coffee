package com.coffee.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreatePaymentLinkResponse {

    private String link;
}

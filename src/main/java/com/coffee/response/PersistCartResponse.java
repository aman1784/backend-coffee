package com.coffee.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersistCartResponse {

    private String userId;
    private List<PersistCartOrderItemResponse> cartItems;
}

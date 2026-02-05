package com.coffee.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCartResponse {

    private String userId;
    private int totalNumberOfItemsPresent;
    List<PersistCartOrderItemResponse> cartItems;
}

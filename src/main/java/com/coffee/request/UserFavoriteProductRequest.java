package com.coffee.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserFavoriteProductRequest extends BaseRequest{

    private String userId;
    private String menuItemKey;
    private String variantKey;

}

package com.coffee.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@Setter
@Getter
@ToString
public class CreateShareCartLinkRequest extends BaseRequest {

    private String userKey;
}

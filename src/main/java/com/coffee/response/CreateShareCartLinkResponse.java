package com.coffee.response;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@Setter
@ToString
@Data
@Builder
public class CreateShareCartLinkResponse {

    private String shareCartLink;
    private String createdByUserName;

}

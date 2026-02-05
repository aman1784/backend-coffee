package com.coffee.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CheckUserNameResponse {

    private String userName;
    private boolean userNameExists;
}

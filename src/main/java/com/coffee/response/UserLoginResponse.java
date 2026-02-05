package com.coffee.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserLoginResponse {

    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String userKey;
    private String userId;
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String emailId;
    private String phoneNumber;
    private String userType;
    private String profilePictureUrl;
}

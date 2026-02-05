package com.coffee.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest extends BaseRequest{

    // either "FORGOT PASSWORD" or "RESET PASSWORD"
    private String changePasswordType;

    // Required if changePasswordType is "FORGOT PASSWORD" or "RESET PASSWORD".
    // EmailId will be passed from the front end itself as the user is already logged in
    private String emailId;

    // Required if changePasswordType is "FORGOT PASSWORD"
    private String phoneNumber;

    // Required if changePasswordType is "RESET PASSWORD"
    private String oldPassword;

    // Required
    private String newPassword;
}

package com.coffee.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest extends BaseRequest {

    @NotBlank(message = "Email id or phone number must not be blank")
    @NotNull(message = "Email id or phone number must not be null")
    private String emailIdOrPhoneNumber;

    @NotBlank(message = "Password must not be blank")
    @NotNull(message = "Password must not be null")
    private String password;
}

package com.coffee.request;

import jakarta.validation.constraints.Email;
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
public class UserSignUpRequest extends BaseRequest{

    @NotNull(message = "First name must not be null")
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    private String middleName;
    private String lastName;

    @NotNull(message = "User name must not be null and greater than 4 characters")
    @NotBlank(message = "User name must not be blank and greater than 4 characters")
    private String userName;

    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Valid email id must be provided")
    private String emailId;

    @NotNull(message = "Phone must not be null and contains 10 digits")
    @NotBlank(message = "Phone must not be blank and contains 10 digits")
    private String phone;

    @NotNull(message = "Password must not be null and greater than 8 characters")
    @NotBlank(message = "Password must not be blank and greater than 8 characters")
    private String password;

    @NotNull(message = "Confirm Password must not be null and greater than 8 characters")
    @NotBlank(message = "Confirm Password must not be blank and greater than 8 characters")
    private String confirmPassword;

    private String dateOfBirth;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;

}

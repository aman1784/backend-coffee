package com.coffee.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NotNull
@Builder
public class UserSignUpResponse {

    private String userKey;
    private String userId;
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String emailId;
    private String phoneNumber;
    private String countryCode;
    private String userType;
    private String accountCreationMessage;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    private OffsetDateTime createdAt;
}

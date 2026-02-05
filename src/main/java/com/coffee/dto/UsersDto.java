package com.coffee.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDto {

    private Long id;
    private String userKey;
    private String userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String userName;
    private String countryCode;
    private String phoneNumber;
    private String emailId;
    private String userType;
    private String dateOfBirth;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String profilePictureUrl;
}


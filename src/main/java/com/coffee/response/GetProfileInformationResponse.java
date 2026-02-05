package com.coffee.response;

import lombok.*;

/**
 * @author Aman Kumar Seth
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProfileInformationResponse {

    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String emailId;
    private String phoneNumber;
    private String dateOfBirth;
    private String userType;
    private String profilePictureUrl;
    private String userName;

}

package com.coffee.response;

import lombok.*;

/**
 * @author Aman Kumar Seth
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditProfileInformationResponse {

    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String userKey;
    private String userId;
    private String profilePictureUrl;
}

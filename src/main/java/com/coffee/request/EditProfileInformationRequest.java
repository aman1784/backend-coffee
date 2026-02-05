package com.coffee.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Aman Kumar Seth
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EditProfileInformationRequest extends BaseRequest{

    private String userKey; // mandatory

    // FrontEnd will send null or empty string ("") if a user doesn't want to update the first name or the middle name or the last name,
    // So backend will keep the existing first name or the existing middle name or the existing last name
    private String newFirstName; // optional
    private String newMiddleName; // optional
    private String newLastName; // optional
    private String newEmailId; // optional -> not implemented
    private String newPhoneNumber; // optional -> not implemented
    private MultipartFile newProfilePicture;
}

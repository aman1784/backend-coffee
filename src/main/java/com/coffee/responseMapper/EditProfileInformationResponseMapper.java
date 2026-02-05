package com.coffee.responseMapper;

import com.coffee.dto.UsersDto;
import com.coffee.dto.UsersPasswordDto;
import com.coffee.request.EditProfileInformationRequest;
import com.coffee.response.EditProfileInformationResponse;
import com.coffee.util.UsersUtility;
import org.springframework.stereotype.Component;

/**
 * @author Aman Kumar Seth
 */

@Component
public class EditProfileInformationResponseMapper {

    public UsersDto mapToUsersDto(EditProfileInformationRequest request, UsersDto usersDto) {
        if (request.getNewFirstName() != null && !request.getNewFirstName().isEmpty()) {
            usersDto.setFirstName(request.getNewFirstName());
        }
        if (request.getNewMiddleName() != null) {
            usersDto.setMiddleName(request.getNewMiddleName());
        }
        if (request.getNewLastName() != null && !request.getNewLastName().isEmpty()) {
            usersDto.setLastName(request.getNewLastName());
        }
        usersDto.setFullName(UsersUtility.generateFullName(request.getNewFirstName(), request.getNewMiddleName(), request.getNewLastName()));
        return usersDto;
    }

    public EditProfileInformationResponse mapToEditProfileInformationResponse(UsersDto usersDto) {
        return EditProfileInformationResponse.builder()
                .firstName(usersDto.getFirstName())
                .middleName(usersDto.getMiddleName())
                .lastName(usersDto.getLastName())
                .fullName(usersDto.getFullName())
                .userKey(usersDto.getUserKey())
                .userId(usersDto.getUserId())
                .profilePictureUrl(usersDto.getProfilePictureUrl())
                .build();
    }
}

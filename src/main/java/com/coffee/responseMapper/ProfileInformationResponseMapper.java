package com.coffee.responseMapper;

import com.coffee.dto.UsersDto;
import com.coffee.response.GetProfileInformationResponse;
import org.springframework.stereotype.Component;

/**
 * @author Aman Kumar Seth
 */

@Component
public class ProfileInformationResponseMapper {

    public GetProfileInformationResponse mapToGetProfileInformationResponse(UsersDto usersDto) {

        return GetProfileInformationResponse.builder()
                .firstName(usersDto.getFirstName())
                .middleName((usersDto.getMiddleName() == null || usersDto.getMiddleName().isEmpty()) ? null : usersDto.getMiddleName())
                .lastName(usersDto.getLastName())
                .fullName(usersDto.getFullName())
                .emailId(usersDto.getEmailId())
                .phoneNumber(usersDto.getPhoneNumber())
                .dateOfBirth(usersDto.getDateOfBirth())
                .userType(usersDto.getUserType())
                .profilePictureUrl(usersDto.getProfilePictureUrl())
                .userName(usersDto.getUserName())
                .build();
    }
}

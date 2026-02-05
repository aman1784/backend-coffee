package com.coffee.usecase.impl;

import com.coffee.dropbox.DropboxService;
import com.coffee.dto.UsersDto;
import com.coffee.exception.GenericException;
import com.coffee.request.EditProfileInformationRequest;
import com.coffee.response.BaseResponse;
import com.coffee.response.EditProfileInformationResponse;
import com.coffee.responseMapper.EditProfileInformationResponseMapper;
import com.coffee.service.UsersService;
import com.coffee.usecase.EditProfileInformationUseCaseService;
import com.coffee.util.ExceptionConstant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Aman Kumar Seth
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class EditProfileInformationUseCaseServiceImpl implements EditProfileInformationUseCaseService {

    private final EditProfileInformationResponseMapper editProfileInformationResponseMapper;

    private final UsersService usersService;
    private final DropboxService dropboxService;

    @Transactional
    @Override
    public BaseResponse<EditProfileInformationResponse> editProfileInformation(EditProfileInformationRequest request) {
        if (request.getUserKey() == null || request.getUserKey().isEmpty()) {
            throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "User key is required");
        }
        UsersDto usersDto = usersService.findByUserKey(request.getUserKey());
        log.info("[EditProfileInformationUseCaseServiceImpl][editProfileInformation] usersDto: {}", usersDto);
        if (usersDto == null) {
            throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "User not found");
        }
        if (request.getNewProfilePicture() != null && !request.getNewProfilePicture().isEmpty()) {
            try {
                String uploadDir = "/images/";
                String fileName = UUID.randomUUID() + "_" + request.getNewProfilePicture().getOriginalFilename();
                String filePath = uploadDir + fileName;
//                Path filePath = Paths.get(uploadDir + fileName);
//                Files.createDirectories(filePath.getParent());
//                Files.copy(request.getNewProfilePicture().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Upload with overwrite mode
                dropboxService.uploadImage(request.getNewProfilePicture(), filePath);
                log.debug("[EditProfileInformationUseCaseServiceImpl][editProfileInformation] File uploaded successfully!");

                // Get public sharable link
                String link = dropboxService.getSharableLink(filePath);
                log.debug("[EditProfileInformationUseCaseServiceImpl][editProfileInformation] Public URL: " + link);

                // Save file path or URL in user DTO
                usersDto.setProfilePictureUrl(link);
            } catch (Exception e) {
                log.debug("[EditProfileInformationUseCaseServiceImpl][editProfileInformation][Debug] Failed to store profile picture", e);
                log.error("[EditProfileInformationUseCaseServiceImpl][editProfileInformation][Error] Failed to store profile picture", e);
                throw new GenericException(ExceptionConstant.otherError, "400", "Other Error While uploading profile picture", "[Could not upload profile picture]: " + e.getMessage());
            }
        }
        if (request.getNewProfilePicture() == null) {
            usersDto.setProfilePictureUrl("");
        }
        usersDto = editProfileInformationResponseMapper.mapToUsersDto(request, usersDto);
        usersDto = usersService.update(usersDto);
        log.debug("[EditProfileInformationUseCaseServiceImpl][editProfileInformation] updated usersDto: {}", usersDto);
        EditProfileInformationResponse response = editProfileInformationResponseMapper.mapToEditProfileInformationResponse(usersDto);
        return BaseResponse.<EditProfileInformationResponse>builder().data(response).build();
    }
}

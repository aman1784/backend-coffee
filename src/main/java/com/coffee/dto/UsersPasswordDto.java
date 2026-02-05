package com.coffee.dto;

import com.coffee.enums.USER_ROLES;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersPasswordDto {

    private Long id;
    private String password;
    private String emailId;
    private String phoneNumber;
    private String userKey;
    private String userId;
    private String userName;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

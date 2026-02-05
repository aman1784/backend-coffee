package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_key", nullable = false, unique = true, length = 255)
    private String userKey;

    @Column(name = "user_id", nullable = false, unique = true, length = 72)
    private String userId;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "middle_name", length = 255)
    private String middleName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "user_name", nullable = false, unique = true, length = 255)
    private String userName;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "email_id", nullable = false, unique = true, length = 255)
    private String emailId;

    @Column(name = "user_type", nullable = false, length = 72)
    private String userType;

    @Column(name = "date_of_birth", length = 72)
    private String dateOfBirth;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private OffsetDateTime updatedAt;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

}

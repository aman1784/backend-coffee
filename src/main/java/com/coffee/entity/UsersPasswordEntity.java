package com.coffee.entity;

import com.coffee.enums.USER_ROLES;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "passwords")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersPasswordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email_id", length = 255)
    private String emailId;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "user_key", length = 255)
    private String userKey;

    @Column(name = "user_id", length = 72)
    private String userId;

    @Column(name = "user_name", length = 255)
    private String userName;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private OffsetDateTime updatedAt;

}

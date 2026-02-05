package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "user_key", nullable = false, length = 255)
    private String user;

//    @OneToOne(fetch = FetchType.LAZY, optional = false) // no cascade persist needed
//    @JoinColumn(name = "user_key", referencedColumnName = "user_key", nullable = false)
//    private UsersPasswordEntity user;
}

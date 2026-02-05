package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@Entity
@Table(name = "share_cart_links")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareCartLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "share_cart_link", unique = true, nullable = false)
    private String shareCartLink;

    @Column(name = "created_by_user_id", nullable = false)
    private String createdByUserId;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}

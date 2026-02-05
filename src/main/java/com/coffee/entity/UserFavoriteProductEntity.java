package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_favorite_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 72)
    private String userId;

    @Column(name = "menu_item_key", nullable = false, length = 72)
    private String menuItemKey;

    @Column(name = "variant_key", nullable = false, length = 72)
    private String variantKey;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}

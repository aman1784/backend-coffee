package com.coffee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 */

@Entity
@Table(name = "order_item_ratings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "menu_item_key", nullable = false)
    private String menuItemKey;

    @Column(name = "rating")
    private double rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "image")
    private String image;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "user_name")
    private String userName; // createdBy
}

package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true, length = 72)
    private String orderId;

    @Column(name = "order_key", nullable = false, unique = true, length = 255)
    private String orderKey;

    @Column(name = "customer_name", nullable = false, length = 255)
    private String customerName;

    @Column(name = "customer_phone_number", nullable = false, length = 15)
    private String customerPhoneNumber;

    @Column(name = "sub_total", nullable = false, precision = 9, scale = 2)
    private BigDecimal subTotal;

    @Column(name = "gst", nullable = false, precision = 9, scale = 2)
    private BigDecimal gst;

    @Column(name = "discount", nullable = false, precision = 9, scale = 2)
    private BigDecimal discount;

    @Column(name = "total", nullable = false, precision = 9, scale = 2)
    private BigDecimal total;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Column(name = "user_id", nullable = false, length = 72)
    private String userId;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "payment_link")
    private String paymentLink;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_type")
    private String paymentType; // Cash or Online
}

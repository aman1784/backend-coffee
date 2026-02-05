package com.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payment_links")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "user_key")
    private String userKey;

    @Column(name = "notes")
    private String notes;

    @Column(name = "description")
    private String description;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "url")
    private String url;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Column(name = "currency")
    private String currency;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "status")
    private int status;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_key")
    private String orderKey;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "error_reason")
    private String errorReason;

    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "error_source")
    private String errorSource;

    @Column(name = "error_step")
    private String errorStep;
}

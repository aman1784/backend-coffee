package com.coffee.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    PENDING(1, "Pending"),
    PAID(2,"Paid"),
    UNPAID(3, "Unpaid"),
    PARTIALLY_PAID(4, "Partially Paid"),
    REFUNDED(5, "Refunded"),
    PARTIALLY_REFUNDED(6,"Partially Refunded"),
    CANCELLED(7,"Cancelled"),
    EXPIRED(8, "Expired"),
    FAILED(9, "Failed");

    private final String value;
    private final int code;

    PaymentStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }
}

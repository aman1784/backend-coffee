package com.coffee.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    ORDER_PLACED(1, "Placed"),
    ORDER_CONFIRMED(2,"Confirmed"),
    ORDER_CANCELLED(3,"Cancelled"),
    ORDER_COMPLETED(4, "Completed"),
    ORDER_DELIVERED(5, "Delivered"),
    ORDER_READY(6, "Ready"),
    ORDER_PICKED_UP(7, "Picked Up"),
    ORDER_IN_TRANSIT(8, "In Transit"),
    ORDER_READY_FOR_PICKUP(9, "Ready For Pickup"),
    ORDER_READY_FOR_DELIVERY(10, "Ready For Delivery"),
    ORDER_READY_FOR_COLLECTION(11, "Ready For Collection");

    private final String value;
    private final int code;

    OrderStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }
}

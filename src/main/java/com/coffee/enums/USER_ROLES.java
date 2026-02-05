package com.coffee.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum USER_ROLES {
    CUSTOMER("CUSTOMER"),
    STAFF("STAFF"),
    MANAGER("MANAGER");

    private final String value;

    USER_ROLES(String value) {
        this.value = value;
    }
}


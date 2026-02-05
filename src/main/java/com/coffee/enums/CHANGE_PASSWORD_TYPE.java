package com.coffee.enums;

import lombok.Getter;

@Getter
public enum CHANGE_PASSWORD_TYPE {

    FORGOT_PASSWORD("Forgot Password"),
    RESET_PASSWORD("Reset Password");

    private final String label;

    CHANGE_PASSWORD_TYPE(String label) {
        this.label = label;
    }
}

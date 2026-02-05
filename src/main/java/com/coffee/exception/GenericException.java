package com.coffee.exception;

import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {

    private final int code;
    private final String status;
    private final String message;
    private final String description;

    public GenericException(int code, String status, String message, String description) {
        super(message);
        this.code = code;
        this.status = status;
        this.message = message;
        this.description = description;
    }
}

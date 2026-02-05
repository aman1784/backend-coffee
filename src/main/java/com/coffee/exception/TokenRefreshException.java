package com.coffee.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

    private final int code;
    private final String status;
    private final String message;
    private final String description;

    public TokenRefreshException(int code, String status, String message, String description) {
        super(message);
        this.code = code;
        this.status = status;
        this.message = message;
        this.description = description;
    }

}
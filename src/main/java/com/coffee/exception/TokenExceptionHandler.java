package com.coffee.exception;

import com.coffee.response.BaseResponse;
import com.coffee.response.ErrorResponse;
//import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// This class will catch our custom TokenException exception
@RestControllerAdvice
public class TokenExceptionHandler {

//    @ExceptionHandler(TokenRefreshException.class)
//    public ResponseEntity<Map<String, String>> handleTokenRefreshException(TokenRefreshException ex) {
//        // Return a 403 Forbidden with a clear error message
//        Map<String, String> errorResponse = Map.of(
//                "error", "Invalid Refresh Token",
//                "message", ex.getMessage()
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleTokenRefreshException(TokenRefreshException ex) {
        // Return a 403 Forbidden with a clear error message
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getDescription());
        BaseResponse<ErrorResponse> response = BaseResponse.<ErrorResponse>builder()
                .code(ex.getCode())
                .status(ex.getStatus())
                .message(ex.getMessage())
                .data(errorResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<BaseResponse<ErrorResponse>> handleExpiredJwtException(ExpiredJwtException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "Token Session Expired");
//        BaseResponse<ErrorResponse> response = BaseResponse.<ErrorResponse>builder()
//                .code(HttpStatus.UNAUTHORIZED.value())
//                .status("Error")
//                .message(ex.getMessage())
//                .data(errorResponse)
//                .build();
//        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//    }
}
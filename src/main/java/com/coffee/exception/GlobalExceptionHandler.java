package com.coffee.exception;

import com.coffee.response.BaseResponse;
import com.coffee.response.ErrorResponse;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                        WebRequest request) {

        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(item -> errors.add(new ErrorResponse(item.getDefaultMessage(), item.getField())));

        BaseResponse<List<ErrorResponse>> response = new BaseResponse<>();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setStatus("error");
        response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setData(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(GenericException.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleGenericException(GenericException ex) {

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getDescription());

        BaseResponse<ErrorResponse> response = BaseResponse.<ErrorResponse>builder()
                .code(ex.getCode())
                .status(ex.getStatus())
                .message(ex.getMessage())
                .data(errorResponse)
                .build();

        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleNotFoundException(ValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);

        BaseResponse<ErrorResponse> response = BaseResponse.<ErrorResponse>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(ex.getLocalizedMessage())
                .message(ex.getMessage())
                .data(errorResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
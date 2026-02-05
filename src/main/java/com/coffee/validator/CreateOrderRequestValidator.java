package com.coffee.validator;

import com.coffee.exception.GenericException;
import com.coffee.request.CreateOrderItemRequest;
import com.coffee.util.ExceptionConstant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CreateOrderRequestValidator {

    public void validateOrderItems(List<CreateOrderItemRequest> orderItems) {
        for (CreateOrderItemRequest orderItem : orderItems) {
            if (orderItem.getItemName() == null || orderItem.getItemName().isEmpty()) {
                throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Item name is required");
            }
            if (orderItem.getItemQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Item quantity must be greater than 0");
            }

            if (orderItem.getItemPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new GenericException(ExceptionConstant.badRequestCode, "Bad Request", ExceptionConstant.badRequestMessage, "Item price must be greater than 0");
            }

        }
    }
}

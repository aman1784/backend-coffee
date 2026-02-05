package com.coffee.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOrderMenuItem {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "MRP must not be null and greater than 0")
    private BigDecimal mrp;

    private String image;
    private String description;

    private boolean enable;
}


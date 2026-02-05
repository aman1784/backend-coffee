package com.coffee.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOrderMenuCategory {

    private Long categoryId;
    private String categoryKey;

    @NotBlank(message = "Name must not be blank")
    private String name;

    private String description;
    private String image;
    private boolean enable;

}

package com.coffee.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMenuItemPageResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<OrderMenuItemResponse> menus;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
}

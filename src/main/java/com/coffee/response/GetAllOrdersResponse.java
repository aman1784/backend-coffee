package com.coffee.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetAllOrdersResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<GetOrderResponse> orders;

    private String page;
    private String size;
    private String totalPages;
    private String totalElements;
}

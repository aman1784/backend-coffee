package com.coffee.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetOrderResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String orderKey;
    private String customerName;
    private String customerPhoneNumber;
    private BigDecimal subTotal;
    private String gstPercentage;
    private BigDecimal gstPrice;
    private BigDecimal discount;
    private BigDecimal total;
    private int status;
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")  // ISO 8601 with timezone
    private OffsetDateTime createdAt;
    private String userId;
    private List<CreateOrderItemResponse> orderItems;
    private String paymentLink;
    private String orderStatus;
    private String paymentStatus;
    private String paymentType;
}

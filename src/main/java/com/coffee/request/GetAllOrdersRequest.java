package com.coffee.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllOrdersRequest extends BaseRequest {

    private String userId;
    private String customerName;
    private String customerPhoneNumber;
    private String fromDate;
    private String toDate;
}

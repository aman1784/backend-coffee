package com.coffee.response;

import lombok.*;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @since 27-01-2026
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareCartDetailsResponse {

    private String createdByUserId;
    private String createdByUserName;
    private int totalNumberOfItemsPresent;
    List<ShareCartDetailItemResponse> cartItems;
}

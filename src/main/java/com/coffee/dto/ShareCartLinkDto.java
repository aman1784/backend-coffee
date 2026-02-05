package com.coffee.dto;

import lombok.*;

import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ShareCartLinkDto {

    private Long id;

    private String shareCartLink;

    private String createdByUserId;

    private String createdBy;

    private int status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}

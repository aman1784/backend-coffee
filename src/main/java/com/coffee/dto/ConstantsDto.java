package com.coffee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author Aman Kumar Seth
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ConstantsDto {

    private Long id;
    private String key;
    private String value;
    private int status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

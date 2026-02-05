package com.coffee.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConstantsDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String key;
    private String value;
}

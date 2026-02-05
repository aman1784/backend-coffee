package com.coffee.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class HomePageDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<ConstantsDataResponse> constants; // <key, value>

}

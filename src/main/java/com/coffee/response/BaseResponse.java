package com.coffee.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<B> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Builder.Default
    @SerializedName("code")
    private int code = HttpStatus.OK.value();

    @Builder.Default
    @SerializedName("status")
    private String status = "success";

    @Builder.Default
    @SerializedName("message")
    private String message = "Request processed successfully";

    @SerializedName("data")
    private B data;


//    @Override
//    public String toString() {
//        return new Gson().toJson(this);
//    }
//    @Override
//    public String toString() {
//        return new GsonBuilder()
//                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
//                .toString();
//    }


    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

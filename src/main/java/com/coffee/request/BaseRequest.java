package com.coffee.request;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class BaseRequest {

    @SerializedName("deviceId")
    private String deviceId;

    @SerializedName("deviceName")
    private String deviceName;

    @SerializedName("source")
    private String source;

    @SerializedName("versionCode")
    private int versionCode;

    @SerializedName("versionName")
    private String versionName;

}

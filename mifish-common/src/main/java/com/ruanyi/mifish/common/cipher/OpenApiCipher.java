package com.ruanyi.mifish.common.cipher;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-08-15 13:53
 */
@Getter
@Setter
public class OpenApiCipher {

    /** appId */
    @JsonProperty("app_id")
    private Integer appId;

    /** appCode */
    @JsonProperty("app_code")
    private String appCode;

    /** apiKey */
    @JsonProperty("app_key")
    private String apiKey;

    /** apiSecret */
    @JsonProperty("api_secret")
    private String apiSecret;

    /**
     * of
     * 
     * @param appId
     * @param appCode
     * @param apiKey
     * @param apiSecret
     * @return
     */
    public static OpenApiCipher of(Integer appId, String appCode, String apiKey, String apiSecret) {
        OpenApiCipher openApiCipher = new OpenApiCipher();
        openApiCipher.appId = appId;
        openApiCipher.appCode = appCode;
        openApiCipher.apiKey = apiKey;
        openApiCipher.apiSecret = apiSecret;
        return openApiCipher;
    }
}

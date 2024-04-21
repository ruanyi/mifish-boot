package com.ruanyi.mifish.media;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2024-04-21 10:47
 */
@Getter
@Setter
public class MediaCallbackModel {

    /** msgId */
    @JsonProperty("msg_id")
    private String msgId;

    /** code */
    @JsonProperty("code")
    private String code;

    /** message */
    @JsonProperty("message")
    private String message;

    /** success */
    @JsonProperty("success")
    private boolean success;

    /** mediaUrls */
    @JsonProperty("media_urls")
    private List<MediaUrl> mediaUrls;

    /** extra */
    @JsonProperty("extra")
    private Map<String, Object> extra;

    /**
     * Description:
     *
     * @author: rls
     * @Date: 2024-04-21 10:47
     */
    @Getter
    @Setter
    public static class MediaUrl {

        /** url */
        @JsonProperty("url")
        private String url;

        /** type */
        @JsonProperty("type")
        private String type;

        /** extra */
        @JsonProperty("extra")
        private Map<String, Object> extra;
    }
}

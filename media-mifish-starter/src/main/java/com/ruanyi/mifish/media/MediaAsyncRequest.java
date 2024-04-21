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
 * @Date: 2024-04-21 10:39
 */
@Getter
@Setter
public class MediaAsyncRequest {

    /** notifyUrl */
    @JsonProperty("notify_url")
    private String notifyUrl;

    /** mediaUrls */
    @JsonProperty("media_urls")
    private List<MediaUrl> mediaUrls;

    /** cmds */
    @JsonProperty("cmds")
    private List<Cmd> cmds;

    /** extra */
    @JsonProperty("extra")
    private Map<String, Object> extra;

    /**
     * Description:
     *
     * @author: rls
     * @Date: 2024-04-21 10:39
     */
    @Getter
    @Setter
    public static class Cmd {

        /** cmd */
        @JsonProperty("cmd")
        private String cmd;

        /** args */
        @JsonProperty("args")
        private Map<String, Object> args;
    }

    /**
     * Description:
     *
     * @author: rls
     * @Date: 2024-04-21 10:39
     */
    @Getter
    @Setter
    public static class MediaUrl {

        /** url */
        @JsonProperty("url")
        private String url;

        /** args */
        @JsonProperty("args")
        private Map<String, Object> args;

    }
}

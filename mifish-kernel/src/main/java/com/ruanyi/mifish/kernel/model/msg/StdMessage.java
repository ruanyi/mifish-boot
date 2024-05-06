package com.ruanyi.mifish.kernel.model.msg;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 23:25
 */
@Getter
@Setter
public class StdMessage {

    /** createTime */
    @JsonProperty("create_time")
    private long createTime;

    /** msgId */
    @JsonProperty("msg_id")
    private String msgId;

    /** notifyUrl */
    @JsonProperty("notify_url")
    private String notifyUrl;

    /** files */
    @JsonProperty("input_files")
    private List<InputFile> inputFiles;

    /** cmds */
    @JsonProperty("cmds")
    private List<FnCmd> cmds;

    /** args */
    @JsonProperty("extra")
    private Map<String, Object> extra;

    /** priority */
    @JsonProperty("priority")
    private Integer priority;
}

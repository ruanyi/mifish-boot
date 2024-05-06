package com.ruanyi.mifish.model.fn;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-04 10:52
 */
public class FnInput {

    /** workPath */
    @JsonProperty("work_path")
    private String workPath;

    /** inputFiles */
    @JsonProperty("input_files")
    private List<InputFile> inputFiles;

    /** arg_map */
    @JsonProperty("arg_map")
    private Map<String, Object> argMap;

    /**
     * Description:
     *
     * @author: ruanyi
     * @Date: 2024-05-04 10:52
     */
    @Getter
    @Setter
    public static class InputFile {

        /** key */
        @JsonProperty("arg_map")
        private String key;

        /** lcoalPath */
        @JsonProperty("local_path")
        private String lcoalPath;

        /** remoteUrl */
        @JsonProperty("remote_url")
        private String remoteUrl;

        /** preDownload */
        @JsonProperty("pre_download")
        private boolean preDownload;
    }
}

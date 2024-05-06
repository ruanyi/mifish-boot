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
 * @Date: 2024-05-04 10:56
 */
@Getter
@Setter
public class FnOutput {

    /** code */
    private String code;

    /** message */
    private String message;

    /** outputFiles */
    @JsonProperty("output_files")
    private List<OutputFile> outputFiles;

    /** arg_map */
    @JsonProperty("arg_map")
    private Map<String, Object> argMap;

    /**
     * Description:
     *
     * @author: ruanyi
     * @Date: 2024-05-04 10:56
     */
    @Getter
    @Setter
    public static class OutputFile {

        /** key */
        private String key;

        /** from */
        private String from;

        /** body */
        private Object body;
    }
}

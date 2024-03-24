package com.ruanyi.mifish.flow.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2018-02-23 19:14
 */
public enum PEventStatus {

    /** 运行成功 */
    SUCCESS("00", "运行成功"),

    /** 正在运行中 */
    RUNNING("01", "正在运行"),

    /** 运行失败 */
    FAILURE("09", "运行失败"),

    /** PIPELINE_NOT_EXIST */
    PIPELINE_NOT_EXIST("90", "业务流不存在!"),

    /** UNHIT_ANY_RULE */
    UNHIT_ANY_RULE("91", "未命中任何一条规则!"),

    /** RULE_NOT_CONFIG */
    RULE_NOT_CONFIG("92", "未配置任何一条场景规则!");

    /**
     * code
     */
    private String code;

    /**
     * desc
     */
    private String desc;

    /**
     * PEventStatus
     *
     * @param code
     * @param desc
     */
    PEventStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * getCode
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * getDesc
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * of
     *
     * @param code
     * @return
     */
    public static PEventStatus of(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (PEventStatus status : values()) {
                if (StringUtils.equals(status.code, code)) {
                    return status;
                }
            }
        }
        return null;
    }
}

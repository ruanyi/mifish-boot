package com.ruanyi.mifish.common.model;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2017-12-29 18:06
 */
public enum ScriptType {

    /** GROOVY */
    GROOVY(1, "groovy脚本"),

    /** JS */
    JS(2, "js脚本"),

    /** PYTHON2 */
    PYTHON2(3, "python2脚本"),

    /** PYTHON3 */
    PYTHON3(4, "python3脚本");

    /**
     * code
     */
    private int code;

    /**
     * desc
     */
    private String desc;

    /**
     * ScriptType
     *
     * @param code
     * @param desc
     */
    ScriptType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * getCode
     *
     * @return
     */
    public int getCode() {
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
    public static ScriptType of(int code) {
        for (ScriptType st : values()) {
            if (st.getCode() == code) {
                return st;
            }
        }
        return null;
    }
}

package com.ruanyi.mifish.common.ex;

/**
 * 遵循阿里巴巴Java开发手册的错误码定义规则。<br>
 * <p>
 * 错误码为字符串类型，共5位。第一位为字符串A/B/C，后四位为数字编号，从0001到9999。大类之间的步长预留100. <br>
 * A: 表示用户错误，比如传参错误，用户访问频次过高，版本过低等。 <br>
 * B: 表示当前系统错误，一般是系统健壮性差，或者业务逻辑出错。 <br>
 * C: 表示依赖系统，调用的第三方服务错误。
 *
 * @author: ruanyi
 * @Date: 2020-04-03 16:44
 */
public enum ErrorCode {

    /** 成功 */
    SUCCESS("00000", "处理成功!"),

    ILLEGAL_ARGUMENT("A0000", "请求参数不合法!"),

    // 参数缺失
    CLIENT_PARAM_MISS("A0002", "param miss"),

    OPERATE_DB_EXCEPTION("B9997", "操作db异常！"),

    SYSTEM_EXCEPTION("B9998", "系统异常！"),

    UNKNOW_EXCEPTION("B9999", "未知异常！"),

    /**
     * 以下是C类当前系统的错误码
     */
    ;

    /** code */
    private String code;

    /** desc */
    private String desc;

    /**
     * ErrorCode
     *
     * @param code
     * @param desc
     */
    ErrorCode(String code, String desc) {
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
     * getMessage
     *
     * @return
     */
    public String getMessage() {
        return this.code + ":" + this.desc;
    }

    /**
     * isSuccess
     *
     * @return
     */
    public boolean isSuccess() {
        return SUCCESS.code.equals(this.code);
    }
}

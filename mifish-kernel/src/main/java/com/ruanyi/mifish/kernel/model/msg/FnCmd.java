package com.ruanyi.mifish.kernel.model.msg;

import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;

import com.ruanyi.mifish.common.annotation.OpenApi;
import com.ruanyi.mifish.common.utils.JacksonUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * std协议中：cmd里的一个字段
 *
 * 切勿随便挪位置，指令引擎中有使用
 *
 * @author: ruanyi
 * @Date: 2019-08-22 19:40
 */
@Getter
@Setter
@OpenApi(scene = "函数命令")
public class FnCmd {

    /** fnName */
    private String fnName;

    /** fnVersion */
    private String fnVersion;

    /** fnUrl */
    private String fnUrl;

    /** args */
    private LinkedHashMap<String, Object> args = new LinkedHashMap<>();

    public FnCmd() {

    }

    /**
     * addArg
     *
     * @param key
     * @param value
     * @return
     */
    public FnCmd addArg(String key, Object value) {
        if (StringUtils.isNotBlank(key) && value != null) {
            this.args.put(key, value);
        }
        return this;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJSONString(this);
    }
}

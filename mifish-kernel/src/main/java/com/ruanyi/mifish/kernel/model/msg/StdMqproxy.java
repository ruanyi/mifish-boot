package com.ruanyi.mifish.kernel.model.msg;

import com.ruanyi.mifish.common.utils.JacksonUtils;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-03-20 14:13
 */
public class StdMqproxy {

    /** stdMessage */
    private StdMessage stdMessage;

    /** host */
    private String host;

    /** token */
    private String token;

    /** key */
    private String key;

    /**
     * getStdMessage
     *
     * @return
     */
    public StdMessage getStdMessage() {
        return stdMessage;
    }

    /**
     * getStdString
     * 
     * @return
     */
    public String getStdString() {
        return JacksonUtils.toJSONString(this.stdMessage);
    }

    /**
     * getHost
     * 
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     * getToken
     * 
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * getKey
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * setKey
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * from
     * 
     * @param stdMessage
     * @param host
     * @param token
     * @return
     */
    public static StdMqproxy from(StdMessage stdMessage, String host, String token) {
        StdMqproxy stdMqproxy = new StdMqproxy();
        stdMqproxy.stdMessage = stdMessage;
        stdMqproxy.host = host;
        stdMqproxy.token = token;
        return stdMqproxy;
    }

    /** StdMqproxy */
    private StdMqproxy() {

    }
}

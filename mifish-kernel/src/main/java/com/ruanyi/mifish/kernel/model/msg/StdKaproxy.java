package com.ruanyi.mifish.kernel.model.msg;

import com.ruanyi.mifish.common.utils.JacksonUtils;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-03-20 14:13
 */
public class StdKaproxy {

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
     * getKaproxyHost
     * 
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     * getKaproxyToken
     * 
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * getKaproxyKey
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * setKaproxyKey
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
    public static StdKaproxy from(StdMessage stdMessage, String host, String token) {
        StdKaproxy stdKaproxy = new StdKaproxy();
        stdKaproxy.stdMessage = stdMessage;
        stdKaproxy.host = host;
        stdKaproxy.token = token;
        return stdKaproxy;
    }

    /** StdKaproxy */
    private StdKaproxy() {

    }
}

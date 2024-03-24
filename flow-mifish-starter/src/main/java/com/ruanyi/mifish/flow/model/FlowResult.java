package com.ruanyi.mifish.flow.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-03-24 19:12
 */
public class FlowResult {

    /** 本次服务是否正常处理 */
    private boolean success = true;

    /** message */
    private String message;

    /** attributes */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * FlowResult
     *
     * @param success
     * @param message
     */
    public FlowResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * FlowResult
     *
     * @param success
     */
    public FlowResult(boolean success) {
        this.success = success;
    }

    /**
     * isSuccess
     *
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * setSuccess
     *
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * getMessage
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * setMessage
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * addAttribute
     *
     * @param key
     * @param value
     */
    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * addAttributes
     *
     * @param attributes
     */
    public void addAttributes(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }
        this.attributes.putAll(attributes);
    }

    /**
     * getAttribute
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAttribute(String key, Class<T> clazz) {
        Object obj = this.attributes.get(key);
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }
}

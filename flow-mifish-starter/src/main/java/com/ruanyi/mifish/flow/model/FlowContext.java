package com.ruanyi.mifish.flow.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-01-22 16:34
 */
public class FlowContext {

    /** inputs */
    private Map<String, Object> inputs = new HashMap<>();

    /** attributes */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * getInputs
     *
     * @return
     */
    public Map<String, Object> getInputs() {
        return inputs;
    }

    /**
     * setInputs
     *
     * @param inputs
     */
    public void setInputs(Map<String, Object> inputs) {
        this.inputs = inputs;
    }

    /**
     * addInput
     *
     * @param key
     * @param value
     */
    public FlowContext addInput(String key, Object value) {
        if (StringUtils.isNotBlank(key) && value != null) {
            this.inputs.put(key, value);
        }
        return this;
    }

    /**
     * isContainInput
     *
     * @param key
     * @return
     */
    public boolean isContainInput(String key) {
        return this.inputs.containsKey(key);
    }

    /**
     * getInput
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getInput(String key, Class<T> clazz) {
        if (isContainInput(key)) {
            return clazz.cast(this.inputs.get(key));
        }
        return null;
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

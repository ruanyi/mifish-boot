package com.ruanyi.mifish.common.model;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-01-03 13:40
 */
public class OperateResult implements Serializable {

    /**
     * success
     */
    private boolean success;

    /**
     * message
     */
    private String message;

    /**
     * data
     */
    private Object data;

    /**
     * OperateResult
     *
     * @param success
     * @param message
     * @param data
     */
    private OperateResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
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
     * getMessage
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * getData
     *
     * @return
     */
    public Object getData() {
        return data;
    }

    /**
     * 获取指定类的透传数据
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getData(Class<T> clazz) {
        return clazz.cast(this.data);
    }

    /**
     * SUCCESS
     *
     * @param data
     * @return
     */
    public static OperateResult SUCCESS(Object data) {
        return new OperateResult(true, "success", data);
    }

    /**
     * SUCCESS
     *
     * @param message
     * @return
     */
    public static OperateResult SUCCESS(String message) {
        return new OperateResult(true, message, null);
    }

    /**
     * FAILURE
     *
     * @param message
     * @param data
     * @return
     */
    public static OperateResult FAILURE(String message, Object data) {
        return new OperateResult(false, message, data);
    }

    /**
     * FAILURE
     *
     * @param message
     * @return
     */
    public static OperateResult FAILURE(String message) {
        return new OperateResult(false, message, null);
    }
}

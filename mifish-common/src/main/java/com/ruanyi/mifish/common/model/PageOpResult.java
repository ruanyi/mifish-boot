package com.ruanyi.mifish.common.model;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 10:59
 */
public class PageOpResult implements Serializable {

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
     * pageSize
     */
    private int pageSize = 10;

    /**
     * pageNum
     */
    private int pageNum = 1;

    /**
     * totalItems
     */
    private int pageTotal = 1;

    /**
     * PageOpResult
     *
     * @param success
     * @param message
     * @param data
     */
    private PageOpResult(boolean success, String message, Object data) {
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
     * getPageSize
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * getPageNum
     *
     * @return
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * getPageTotal
     *
     * @return
     */
    public int getPageTotal() {
        return pageTotal;
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
    public static PageOpResult SUCCESS(Object data) {
        if (data instanceof PageList) {
            PageList<?> pageList = (PageList<?>)data;
            PageOpResult op = new PageOpResult(true, "success", data);
            op.pageNum = pageList.getPage();
            op.pageSize = pageList.getPageSize();
            op.pageTotal = pageList.getTotalItems();
            return op;
        }
        return new PageOpResult(true, "success", data);
    }

    /**
     * SUCCESS
     *
     * @param message
     * @return
     */
    public static PageOpResult SUCCESS(String message) {
        return new PageOpResult(true, message, null);
    }

    /**
     * FAILURE
     *
     * @param message
     * @param data
     * @return
     */
    public static PageOpResult FAILURE(String message, Object data) {
        return new PageOpResult(false, message, data);
    }

    /**
     * FAILURE
     *
     * @param message
     * @return
     */
    public static PageOpResult FAILURE(String message) {
        return new PageOpResult(false, message, null);
    }
}

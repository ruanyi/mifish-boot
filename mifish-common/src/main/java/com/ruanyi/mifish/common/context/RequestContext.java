package com.ruanyi.mifish.common.context;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-07-26 12:38
 */
public final class RequestContext {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    /** requestId */
    private final String requestId;

    /** resourceUrl */
    private final String resourceUrl;

    /**
     * RequestContext
     *
     * @param requestId
     * @param resourceUrl
     */
    private RequestContext(String requestId, String resourceUrl) {
        this.requestId = requestId;
        this.resourceUrl = resourceUrl;
    }

    /**
     * getRequestId
     *
     * @return
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * getResourceUrl
     * 
     * @return
     */
    public String getResourceUrl() {
        return resourceUrl;
    }

    /**
     * init
     *
     * @param requestId
     * @param resourceUrl
     */
    public static void init(String requestId, String resourceUrl) {
        RequestContext requestContext = new RequestContext(requestId, resourceUrl);
        CONTEXT.set(requestContext);
    }

    /**
     * 清理本次线程的上线文
     */
    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * 获取当前上下文
     * 
     * @return
     */
    public static RequestContext get() {
        return CONTEXT.get();
    }

    /**
     * getRequestId
     * 
     * @return
     */
    public static String getCurrentRequestId() {
        RequestContext requestContext = CONTEXT.get();
        if (requestContext != null) {
            return requestContext.requestId;
        }
        return null;
    }

    /**
     * getCurrentResourceUrl
     * 
     * @return
     */
    public static String getCurrentResourceUrl() {
        RequestContext requestContext = CONTEXT.get();
        if (requestContext != null) {
            return requestContext.resourceUrl;
        }
        return null;
    }

    /**
     * 一般用于父子线程上下文中使用
     * 
     * @param parentContext
     */
    public static void spawn(RequestContext parentContext) {
        CONTEXT.set(parentContext);
    }
}

package com.ruanyi.mifish.common.context;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-07-26 12:38
 */
public final class RequestContext {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    /**
     * traceId
     */
    private final String traceId;

    /**
     * resourceUrl
     */
    private final String resourceUrl;

    /**
     * RequestContext
     *
     * @param traceId
     * @param resourceUrl
     */
    private RequestContext(String traceId, String resourceUrl) {
        this.traceId = traceId;
        this.resourceUrl = resourceUrl;
    }

    /**
     * getTraceId
     *
     * @return
     */
    public String getTraceId() {
        return traceId;
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
     * @param traceId
     * @param resourceUrl
     */
    public static void init(String traceId, String resourceUrl) {
        RequestContext requestContext = new RequestContext(traceId, resourceUrl);
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
     * getCurrentTraceId
     *
     * @return
     */
    public static String getCurrentTraceId() {
        RequestContext requestContext = CONTEXT.get();
        if (requestContext != null) {
            return requestContext.traceId;
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

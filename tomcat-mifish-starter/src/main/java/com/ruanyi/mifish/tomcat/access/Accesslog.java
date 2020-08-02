package com.ruanyi.mifish.tomcat.access;

import static com.ruanyi.mifish.tomcat.access.CustomAccessLogValve.EXTERNAL_ACCESS_LOG_FIELDS;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ruanyi.mifish.tomcat.utils.MifishHttpUtil;

/**
 * Description:
 *
 * 一般情况下，仅供内部使用，暂不允许大规模外部使用
 *
 * @author: rls
 * @Date: 2018/8/8 10:34
 */
public final class Accesslog {

    private static final String RESP_ERROR_CODE = "response_body_error_code";

    /**
     * append
     *
     * @param request
     * @param fieldName
     * @param value
     */
    public static void append(HttpServletRequest request, String fieldName, Object value) {
        Map<String, Object> fields = getExternalFields(request);
        if (fields == null) {
            synchronized (Accesslog.class) {
                fields = getExternalFields(request);
                if (fields == null) {
                    fields = new HashMap<>(32);
                    request.setAttribute(EXTERNAL_ACCESS_LOG_FIELDS, fields);
                }
            }
        }
        fields.put(fieldName, value);
    }

    /**
     * appendErrorCode
     *
     * @param request
     * @param value
     */
    public static void appendErrorCode(HttpServletRequest request, Object value) {
        append(request, RESP_ERROR_CODE, value);
    }

    /**
     * appendErrorCode
     *
     * @param value
     */
    public static void appendErrorCode(Object value) {
        append(MifishHttpUtil.getHttpServletRequest(), RESP_ERROR_CODE, value);
    }

    /**
     * getExternalFields
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getExternalFields(HttpServletRequest request) {
        Map<String, Object> fields = (Map<String, Object>)request.getAttribute(EXTERNAL_ACCESS_LOG_FIELDS);
        return fields;
    }
}

package com.ruanyi.mifish.kernel.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 15:42
 */
public final class MifishHttpUtils {

    /**
     * getHttpServletRequest
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    /**
     * getHttpServletResponse
     *
     * @return
     */
    public static HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes servletRequestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getResponse();
        }
        return null;
    }

    /**
     * getRequestLocale
     *
     * @return
     */
    public static Locale getRequestLocale() {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (httpServletRequest != null) {
            return RequestContextUtils.getLocale(httpServletRequest);
        }
        return LocaleContextHolder.getLocale();
    }

    /**
     * getHttpSession
     *
     * @return
     */
    public static HttpSession getHttpSession() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return null;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
        }
        return session;
    }

    /**
     * getHttpSessionValue
     *
     * @param key
     * @return
     */
    public static String getHttpSessionValue(String key) {
        HttpSession httpSession = getHttpSession();
        if (httpSession == null) {
            return "";
        }
        Object obj = httpSession.getAttribute(key);
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return (String)obj;
        } else {
            return obj.toString();
        }
    }

    /**
     * getCookieByName
     *
     * @param name
     * @return
     */
    public static Cookie getCookieByName(String name) {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie c : cookies) {
            if (StringUtils.equalsIgnoreCase(c.getName(), name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * getCookieValueByName
     *
     * @param name
     * @return
     */
    public static String getCookieValueByName(String name) {
        Cookie cookie = getCookieByName(name);
        if (cookie == null) {
            return null;
        }
        try {
            String value = cookie.getValue();
            value = URLDecoder.decode(value, "utf-8");
            value = new String(Base64.getDecoder().decode(value), "utf-8");
            return value;
        } catch (UnsupportedEncodingException ex) {
            String value = cookie.getValue();
            value = URLDecoder.decode(value);
            value = new String(Base64.getDecoder().decode(value));
            return value;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * newCookie
     *
     * @param name
     * @param value
     * @param domain
     * @param maxAge
     * @param path
     * @param secured
     * @return
     */
    public static Cookie newCookie(String name, String value, String domain, int maxAge, String path, boolean secured) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(value)) {
            return null;
        }
        String v;

        try {
            v = Base64.getEncoder().encodeToString(value.getBytes("utf-8"));
            v = URLEncoder.encode(v, "utf-8");
        } catch (UnsupportedEncodingException e) {
            v = Base64.getEncoder().encodeToString(value.getBytes());
            v = URLEncoder.encode(v);
        }
        Cookie cookie = new Cookie(name, v);
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(secured);
        return cookie;
    }

    /**
     * newCookie
     *
     * @param name
     * @param value
     * @return
     */
    public static Cookie newCookie(String name, String value) {
        return newCookie(name, value, "ruanyi.github.io", -1, "/", false);
    }

    /**
     * newCookie
     *
     * @param name
     * @param value
     * @param maxAge
     * @param path
     * @return
     */
    public static Cookie newCookie(String name, String value, int maxAge, String path) {
        return newCookie(name, value, "ruanyi.github.io", maxAge, path, false);
    }

    /**
     * getRequestParameter
     *
     * @param key
     * @return
     */
    public static String getRequestParameter(String key) {
        HttpServletRequest request = getHttpServletRequest();
        if (StringUtils.isNotBlank(key) && request != null) {
            return request.getParameter(key);
        }
        return null;
    }

    /**
     * getRequestHeader
     *
     * @param key
     * @return
     */
    public static String getRequestHeader(String key) {
        HttpServletRequest request = getHttpServletRequest();
        if (StringUtils.isNotBlank(key) && request != null) {
            return request.getHeader(key);
        }
        return null;
    }

    /**
     * getRequestAttribute
     *
     * @param key
     * @return
     */
    public static Object getRequestAttribute(String key) {
        HttpServletRequest request = getHttpServletRequest();
        if (StringUtils.isNotBlank(key) && request != null) {
            return request.getAttribute(key);
        }
        return null;
    }

    /**
     * getRequestAttribute
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getRequestAttribute(String key, Class<T> clazz) {
        HttpServletRequest request = getHttpServletRequest();
        if (StringUtils.isNotBlank(key) && request != null) {
            return clazz.cast(request.getAttribute(key));
        }
        return null;
    }

    /**
     * 获取Ip地址
     *
     * @param request
     * @return
     */
    public static String getRequestIp(HttpServletRequest request) {
        String xip = request.getHeader("X-Real-IP");
        String xfor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(xfor) && !"unKnown".equalsIgnoreCase(xfor)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xfor.indexOf(",");
            if (index != -1) {
                return xfor.substring(0, index);
            } else {
                return xfor;
            }
        }
        xfor = xip;
        if (StringUtils.isNotEmpty(xfor) && !"unKnown".equalsIgnoreCase(xfor)) {
            return xfor;
        }
        if (StringUtils.isBlank(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getRemoteAddr();
        }
        return xfor;
    }
}

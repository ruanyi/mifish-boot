package com.ruanyi.mifish.kernel.check;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 15:19
 */
@Getter
@Setter
public class MifishCheckContext {

    /** request */
    private HttpServletRequest request;

    /** response */
    private HttpServletResponse response;

    /** method */
    private Method method;

    /**
     * <p>
     * Returns the target object. This will always be the same object as that matched by the <code>target</code>
     * pointcut designator. Unless you specifically need this reflective access, you should use the <code>target</code>
     * pointcut designator to get at this object for better static typing and performance.
     * </p>
     *
     * <p>
     * Returns null when there is no target object.
     * </p>
     */
    private Object target;

    /**
     * <p>
     * Returns the arguments at this join point.
     * </p>
     */
    private Object[] args;
}

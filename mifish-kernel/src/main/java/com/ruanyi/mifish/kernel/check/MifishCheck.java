package com.ruanyi.mifish.kernel.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 15:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MifishCheck {

    /**
     * 是否进行签名校验
     *
     * @return
     */
    boolean isSign() default true;

    /**
     * 是否进行登录校验
     * 
     * 1、有些接口，不需要登录，也能正产请求
     * 
     * 2、如果不需要登录，则只需要简单的签名校验即可
     *
     * @return
     */
    boolean isAuth() default false;
}

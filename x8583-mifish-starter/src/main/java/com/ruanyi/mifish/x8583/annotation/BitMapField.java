package com.ruanyi.mifish.x8583.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-10 11:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface BitMapField {

    /**
     * 顺序，一般默认情况下，都是：1。
     *
     * @return
     */
    int order() default 1;
}

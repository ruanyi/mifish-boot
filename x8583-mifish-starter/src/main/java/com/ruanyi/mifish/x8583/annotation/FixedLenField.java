package com.ruanyi.mifish.x8583.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ruanyi.mifish.x8583.model.PaddDirect;

/**
 * Description:
 *
 * 固定长度的8583报文
 *
 * @author: rls
 * @Date: 2023-09-10 11:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface FixedLenField {

    /**
     * 顺序
     *
     * @return
     */
    int order();

    /**
     * 报文长度
     *
     * @return
     */
    int length();

    /**
     * paddingWay
     *
     * @return
     */
    PaddDirect paddingWay() default PaddDirect.PADD_LEFT;

    /**
     * 是否是bcd编码
     * 
     * @return
     */
    boolean isBcd() default false;
}

package com.ruanyi.mifish.x8583.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ruanyi.mifish.x8583.model.IntEncodeType;
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
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
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
     * 请配置指定的asccii码值
     * 
     * @return
     */
    byte paddingByte() default 32;

    /**
     * 获取十进制数据后的编码类型
     * 
     * @return
     */
    IntEncodeType intEncodeType() default IntEncodeType.NONE;
}

package com.ruanyi.mifish.x8583.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ruanyi.mifish.x8583.model.IntEncodeType;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-10 11:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface UnsizedField {

    /**
     * 顺序
     *
     * @return
     */
    int order();

    /**
     * 报文消息长度的长度
     * 
     * 左边先读取多少长度的长度
     * 
     * 然后，转成数字len，在读取len的报文体
     * 
     * @return
     */
    int lenlen() default 1;

    /**
     * 获取十进制数据后的编码类型
     *
     * @return
     */
    IntEncodeType intEncodeType() default IntEncodeType.NONE;
}

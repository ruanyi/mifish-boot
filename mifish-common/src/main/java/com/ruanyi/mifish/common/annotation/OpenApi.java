package com.ruanyi.mifish.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记当前类 or 方法 or 某个属性，是对外公开的api，请再次确保外围是否还有使用<br>
 * <p>
 * 被标记的类，方法，或者某个域，不要随便更改：package、方法名等
 *
 * @author: ruanyi
 * @Date: 2020-09-18 14:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface OpenApi {

    /**
     * 标记使用场景
     *
     * @return
     */
    String scene();

    /**
     * 相关备注信息
     *
     * @return
     */
    String desc() default "";
}


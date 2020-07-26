package com.mifish.common.utils;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2018-05-22 11:14
 */
public final class UUIDUtil {

    /**
     * 生成jdk的唯一id集合
     *
     * @return
     */
    public static final String obtainUUID() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replace(uuid, "-", "");
    }

    /**
     * UUIDUtil
     */
    private UUIDUtil() {

    }
}

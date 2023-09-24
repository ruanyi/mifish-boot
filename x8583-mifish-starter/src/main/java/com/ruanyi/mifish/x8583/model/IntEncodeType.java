package com.ruanyi.mifish.x8583.model;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-24 14:43
 */
public enum IntEncodeType {

    /** 压缩型bcd码 */
    COMPRESS_BCD,

    /** 非压缩型bcd码 */
    UNCOMPRESS_BCD,

    /** 不进行bcd编解码操作 */
    NONE;
}

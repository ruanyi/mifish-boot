package com.ruanyi.mifish.x8583.types;

/**
 * BCD压缩 右靠，左补空格
 * 
 * @author ruanyi
 * @time:2013-06-21
 */
public class IF_BCDR extends IF_BCD {

    public IF_BCDR(Integer length) {
        super(length);
        setPaddDirect(PADD_LEFT);
    }
}

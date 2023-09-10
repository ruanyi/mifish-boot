package com.ruanyi.mifish.x8583.types;

/**
 * 右靠、左补
 * 
 * @author ruanyi
 * @time:2013-06-21
 */
public class IF_LLBCDR extends IF_LLBCD {

    public IF_LLBCDR(Integer length) {
        super(length);
        setPaddDirect(PADD_LEFT);
    }
}

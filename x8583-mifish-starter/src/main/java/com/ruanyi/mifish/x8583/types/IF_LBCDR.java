package com.ruanyi.mifish.x8583.types;

/**
 * BCD 右靠，左补
 * 
 * @author ruanyi
 * @time:2013-06-21
 */
public class IF_LBCDR extends IF_LBCD {

    public IF_LBCDR(Integer length) {
        super(length);
        setPaddDirect(PADD_LEFT);
    }

}

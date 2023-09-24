package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.x8583.ISOType;
import com.ruanyi.mifish.x8583.annotation.FixedLenField;
import com.ruanyi.mifish.x8583.model.IntEncodeType;

/**
 * BCD压缩 左靠，右补byte
 * 
 * @author ruanyi
 * @time:2013-06-21
 */
@FixedLenField(order = 1, length = 1, intEncodeType = IntEncodeType.COMPRESS_BCD)
public class IF_BCD implements ISOType {

}

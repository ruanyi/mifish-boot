package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;

/**
 * 
 * IF_LCHAR
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public class IF_LCHAR extends IF_ISOType {

    public IF_LCHAR(Integer length) {
        super(length);
        if (length > 9) {
            throw new RuntimeException("IF_LCHAR support max length is 9,current set to " + length);
        }
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        byte[] value = component.dump();
        int length = value.length;
        if (length > 9) {
            throw new ISOX8583Exception("IF_LCHAR support max length is 9,current set to " + length);
        }
        return BytesUtil.join(new byte[] {(byte)length}, value);
    }

    @Override
    public byte[] read(byte[] msg) {
        byte lbyte = msg[0];
        return BytesUtil.read(msg, lbyte + 1);
    }

    @Override
    public byte[] take(byte[] msg) {
        byte lbyte = msg[0];
        return BytesUtil.remove(msg, lbyte + 1);
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        byte[] value = BytesUtil.remove(values, 1);
        return new BasicISOField(index, value);
    }
}

package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;

/**
 * IF_LLLLCHAR
 * 
 * @author ruanyi
 * @time:2013-06-01
 */
public class IF_LLLLCHAR extends IF_ISOType {

    public IF_LLLLCHAR(Integer length) {
        super(length);
        if (length > 9999) {
            throw new ISOX8583Exception("IF_LLLLCHAR support max length is 9999,current set to " + length);
        }
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        byte[] value = component.dump();
        int length = value.length;
        if (length > 9999) {
            throw new ISOX8583Exception("IF_LLLLCHAR support max length is 9999,current set to " + length);
        }

        return BytesUtil.join(this.to4Length(length), value);
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        byte[] value = BytesUtil.remove(values, 4);
        return new BasicISOField(index, value);
    }

    @Override
    public byte[] read(byte[] msg) {
        byte[] lbyte = BytesUtil.read(msg, 4);
        return BytesUtil.read(msg, BytesUtil.toInteger(lbyte) + 4);
    }

    @Override
    public byte[] take(byte[] msg) {
        byte[] lbyte = BytesUtil.read(msg, 4);
        return BytesUtil.remove(msg, BytesUtil.toInteger(lbyte) + 4);
    }
}

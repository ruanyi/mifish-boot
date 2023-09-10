package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;

/**
 * 
 * IF_LLLCHAR
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public class IF_LLLCHAR extends IF_ISOType {

    public IF_LLLCHAR(Integer length) {
        super(length);
        if (length > 999) {
            throw new ISOX8583Exception("IF_LLLCHAR support max length is 999,current set to " + length);
        }
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        byte[] value = component.dump();
        int length = value.length;
        if (length > 999) {
            throw new ISOX8583Exception("IF_LLLCHAR support max length is 999,current set to " + length);
        }
        return BytesUtil.join(this.to3Length(length), value);
    }

    @Override
    public byte[] read(byte[] msg) {
        byte[] lbyte = BytesUtil.read(msg, 3);
        return BytesUtil.read(msg, BytesUtil.toInteger(lbyte) + 3);
    }

    @Override
    public byte[] take(byte[] msg) {
        byte[] lbyte = BytesUtil.read(msg, 3);
        return BytesUtil.remove(msg, BytesUtil.toInteger(lbyte) + 3);
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) {
        byte[] value = BytesUtil.remove(values, 3);
        return new BasicISOField(index, value);
    }
}

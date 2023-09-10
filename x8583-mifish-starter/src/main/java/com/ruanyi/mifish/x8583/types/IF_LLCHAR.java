package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;

/**
 * 
 * IF_LLCHAR
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public class IF_LLCHAR extends IF_ISOType {

    public IF_LLCHAR(Integer length) {
        super(length);
        if (length > 99) {
            throw new ISOX8583Exception("IF_LLCHAR support max length is 99,current set to " + length);
        }
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        byte[] value = component.dump();
        int length = value.length;
        if (length > 99) {
            throw new ISOX8583Exception("IF_LLCHAR support max length is 99,current set to " + length);
        }

        return BytesUtil.join(this.to2Length(length), value);
    }

    @Override
    public byte[] read(byte[] msg) {
        byte[] lbyte = BytesUtil.read(msg, 2);
        return BytesUtil.read(msg, BytesUtil.toInteger(lbyte) + 2);
    }

    @Override
    public byte[] take(byte[] msg) {
        byte[] lbyte = BytesUtil.read(msg, 2);
        return BytesUtil.remove(msg, BytesUtil.toInteger(lbyte) + 2);
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        byte[] value = BytesUtil.remove(values, 2);
        return new BasicISOField(index, value);
    }
}

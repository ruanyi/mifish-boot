package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.Paddable;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;

/**
 * 
 * L_CHAR
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-2
 */
public class IF_CHAR extends IF_ISOType implements Paddable {

    private String paddDirect = "RIGHT";

    private byte paddByte = " ".getBytes()[0];

    public IF_CHAR(Integer length) {
        super(length);
    }

    @Override
    public byte[] take(byte[] msg) {
        return BytesUtil.remove(msg, this.getLength());
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        byte[] values = component.dump();
        if (values.length > this.getLength()) {
            throw new ISOX8583Exception("current IF_CHAR type support length-->[" + this.getLength() + "]");
        }
        if (PADD_RIGHT.equalsIgnoreCase(this.paddDirect)) {
            return BytesUtil.rightPadd(values, this.paddByte, this.getLength());
        } else {
            return BytesUtil.leftPadd(values, this.paddByte, this.getLength());
        }
    }

    @Override
    public byte[] read(byte[] msg) {
        return BytesUtil.read(msg, getLength());
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        if (values.length > this.getLength()) {
            throw new ISOX8583Exception(
                "current index：【" + index + "】   type 【IF_CHAR】 support length-->[" + this.getLength() + "]");
        }
        return new BasicISOField(index, values);
    }

    @Override
    public void setPaddByte(byte value) {
        this.paddByte = value;
    }

    @Override
    public void setPaddDirect(String direct) {
        this.paddDirect = direct.toUpperCase();
    }
}

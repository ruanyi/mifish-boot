package com.ruanyi.mifish.x8583.types;

import java.io.UnsupportedEncodingException;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.Paddable;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;
import com.ruanyi.mifish.x8583.utils.BCDUtil;

/**
 * BCD压缩 左靠，右补byte
 * 
 * @author ruanyi
 * @time:2013-06-21
 */
public class IF_BCD extends IF_ISOType implements Paddable {

    private String paddDirect = "RIGHT";

    protected byte paddByte = " ".getBytes()[0];

    public IF_BCD(Integer length) {
        super(length);
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        byte[] values = component.dump();
        String str = null;
        try {
            str = new String(values, this.getCharset());
        } catch (UnsupportedEncodingException e) {
            str = new String(values);
        }
        /** 左靠，右补 */
        byte[] zipBytes = null;
        if (PADD_RIGHT.equalsIgnoreCase(this.paddDirect)) {
            zipBytes = BCDUtil.pZipBcd(str.toCharArray(), true);
        } else {
            zipBytes = BCDUtil.pZipBcd(str.toCharArray(), false);
        }
        if (zipBytes.length > this.getLength()) {
            throw new ISOX8583Exception("current IF_BCD type support length-->[" + this.getLength() + "].");
        }
        return zipBytes;
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        if (values.length > this.getLength()) {
            throw new ISOX8583Exception(
                "current index：【" + index + "】   type 【IF_BCD】 support length-->[" + this.getLength() + "]");
        }
        //
        String str = new String(BCDUtil.unZipBcd(values));
        try {
            return new BasicISOField(index, str.getBytes(this.getCharset()));
        } catch (UnsupportedEncodingException e) {
            return new BasicISOField(index, str.getBytes());
        }
    }

    @Override
    public byte[] read(byte[] msg) {
        return BytesUtil.read(msg, getLength());
    }

    @Override
    public byte[] take(byte[] msg) {
        return BytesUtil.remove(msg, this.getLength());
    }

    @Override
    public void setPaddDirect(String direct) {
        this.paddDirect = direct.toUpperCase();
    }

    @Override
    public void setPaddByte(byte value) {
        this.paddByte = value;
    }
}

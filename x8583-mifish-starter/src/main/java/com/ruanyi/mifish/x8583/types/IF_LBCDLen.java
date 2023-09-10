package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.Paddable;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;
import com.ruanyi.mifish.x8583.utils.BCDUtil;

/**
 * 
 * IF_LBCDLen
 * 
 * @author ruanyi
 * @time:2013-06-21
 */
public class IF_LBCDLen extends IF_ISOType implements Paddable {

    protected String paddDirect = "RIGHT";

    protected byte paddByte = " ".getBytes()[0];

    public IF_LBCDLen(Integer length) {
        super(length);
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        byte[] values = component.dump();
        int length = values.length;
        byte[] bcdLen = BCDUtil.pZipBcd(String.valueOf(length).toCharArray(), false);
        if (bcdLen.length > 1) {
            throw new ISOX8583Exception("IF_LBCD support max length is 99,current set to " + length);
        }
        return BytesUtil.join(bcdLen, values);
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        byte[] value = BytesUtil.remove(values, 1);
        return new BasicISOField(index, value);
    }

    @Override
    public byte[] read(byte[] msg) {
        byte lbyte = msg[0];
        int realen = Integer.parseInt(new String(BCDUtil.unZipBcd(new byte[] {lbyte})));
        return BytesUtil.read(msg, realen + 1);
    }

    @Override
    public byte[] take(byte[] msg) {
        byte lbyte = msg[0];
        int realen = Integer.parseInt(new String(BCDUtil.unZipBcd(new byte[] {lbyte})));
        return BytesUtil.remove(msg, realen + 1);
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

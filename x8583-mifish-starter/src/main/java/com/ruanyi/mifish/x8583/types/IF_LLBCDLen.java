package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.Paddable;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;
import com.ruanyi.mifish.x8583.utils.BCDUtil;

/**
 * 
 * IF_LLBCDLen
 * 
 * @author ruanyi
 * @time:2013-06-21
 */
public class IF_LLBCDLen extends IF_ISOType implements Paddable {

    protected String paddDirect = "RIGHT";

    protected byte paddByte = " ".getBytes()[0];

    public IF_LLBCDLen(Integer length) {
        super(length);
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        byte[] values = component.dump();
        int length = values.length;
        String strLen = new String(this.to4Length(length));
        byte[] bcdLen = BCDUtil.pZipBcd(strLen.toCharArray(), false);
        if (bcdLen.length > 2) {
            throw new ISOX8583Exception("IF_LLBCDLen support max length is 9999,current set to " + length);
        }
        return BytesUtil.join(bcdLen, values);
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        byte[] value = BytesUtil.remove(values, 2);
        return new BasicISOField(index, value);
    }

    @Override
    public byte[] read(byte[] msg) {
        byte[] lbytes = BytesUtil.read(msg, 2);
        int msgLen = Integer.parseInt(new String(BCDUtil.unZipBcd(lbytes)));
        return BytesUtil.read(msg, msgLen + 2);
    }

    @Override
    public byte[] take(byte[] msg) {
        byte[] lbytes = BytesUtil.read(msg, 2);
        int msgLen = Integer.parseInt(new String(BCDUtil.unZipBcd(lbytes)));
        int realen = msgLen % 2 + msgLen / 2;
        return BytesUtil.remove(msg, realen + 2);
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

package com.ruanyi.mifish.x8583.types;

import java.io.UnsupportedEncodingException;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.Paddable;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;
import com.ruanyi.mifish.x8583.utils.BCDUtil;

/**
 * IF_LLBCD
 * 
 * @author ruanyi
 * @time:2013-06-21
 */
public class IF_LLBCD extends IF_ISOType implements Paddable {

    private String paddDirect = "RIGHT";

    protected byte paddByte = " ".getBytes()[0];

    public IF_LLBCD(Integer length) {
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
        byte[] bcdBytes = null;
        if (PADD_RIGHT.equalsIgnoreCase(this.paddDirect)) {
            bcdBytes = BCDUtil.pZipBcd(str.toCharArray(), true);
        } else {
            bcdBytes = BCDUtil.pZipBcd(str.toCharArray(), false);
        }
        int length = values.length;
        String lenStr = new String(this.to4Length(length));
        byte[] bcdLen = BCDUtil.pZipBcd(lenStr.toCharArray(), false);
        if (bcdLen.length > 2) {
            throw new ISOX8583Exception("IF_LLBCD support max length is 9999,current set to " + length);
        }
        return BytesUtil.join(bcdLen, bcdBytes);
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        byte[] lbytes = BytesUtil.read(values, 2);
        int msgLen = Integer.parseInt(new String(BCDUtil.unZipBcd(lbytes)));
        String msgStr = new String(BCDUtil.unZipBcd(BytesUtil.remove(values, 2)));
        byte[] msgBytes = null;
        try {
            msgBytes = msgStr.getBytes(this.getCharset());
        } catch (UnsupportedEncodingException e) {
            msgBytes = msgStr.getBytes();
        }
        return new BasicISOField(index, BytesUtil.read(msgBytes, msgLen));
    }

    @Override
    public byte[] read(byte[] msg) {
        byte[] lbytes = BytesUtil.read(msg, 2);
        int msgLen = Integer.parseInt(new String(BCDUtil.unZipBcd(lbytes)));
        int realen = msgLen / 2 + msgLen % 2;
        return BytesUtil.read(msg, realen + 2);
    }

    @Override
    public byte[] take(byte[] msg) {
        byte[] lbytes = BytesUtil.read(msg, 2);
        int msgLen = Integer.parseInt(new String(BCDUtil.unZipBcd(lbytes)));
        int realen = msgLen / 2 + msgLen % 2;
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

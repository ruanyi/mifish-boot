package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.ISOType;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.ISOBitMap;

/**
 * 
 * ISOBitMap
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public class IF_BitMap implements ISOType {

    private int length;

    private String charset = "UTF-8";

    public IF_BitMap() {

    }

    public IF_BitMap(Integer length) {
        this.length = length;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public byte[] take(byte[] msg) {
        return BytesUtil.remove(msg, length);
    }

    @Override
    public byte[] pack(ISOComponent component) throws ISOX8583Exception {
        if (component instanceof ISOBitMap) {
            return component.dump();
        }
        throw new ISOX8583Exception("IF_BitMap only support ISOBitMap Component!!!");
    }

    @Override
    public byte[] read(byte[] msg) {
        byte b = BytesUtil.read(msg, 1)[0];
        int flag = (int)b;
        if (flag < 0) {
            this.setLength(16);
            return BytesUtil.read(msg, 16);
        } else {
            this.setLength(8);
            return BytesUtil.read(msg, 8);
        }
    }

    @Override
    public ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception {
        ISOBitMap bitmap = new ISOBitMap(values);
        bitmap.setIndex(index);
        return bitmap;
    }

    @Override
    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String getCharset() {
        return this.charset;
    }
}

package com.ruanyi.mifish.x8583.types;

import com.ruanyi.mifish.x8583.ISOType;

/**
 * 
 * IF_ISOType
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public abstract class IF_ISOType implements ISOType {

    private int length;

    protected String charset = "UTF-8";

    public IF_ISOType(Integer length) {
        super();
        this.length = length;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    protected byte[] to2Length(int length) {
        if (length <= 9) {
            return ("0" + length).getBytes();
        }
        return String.valueOf(length).getBytes();
    }

    protected byte[] to3Length(int length) {
        if (length <= 9) {
            return ("00" + length).getBytes();
        } else if (length <= 99) {
            return ("0" + length).getBytes();
        }
        return String.valueOf(length).getBytes();
    }

    protected byte[] to4Length(int length) {
        if (length <= 9) {
            return ("000" + length).getBytes();
        } else if (length <= 99) {
            return ("00" + length).getBytes();
        } else if (length <= 999) {
            return ("0" + length).getBytes();
        }
        return String.valueOf(length).getBytes();
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

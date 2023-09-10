package com.ruanyi.mifish.x8583.field;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.BitSet;

import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;

/**
 * 
 * ISOBitMap
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-2
 */
public class ISOBitMap implements ISOComponent {

    protected static byte B_1 = (byte)0x80;

    protected static byte B_2 = (byte)0x40;

    protected static byte B_3 = (byte)0x20;

    protected static byte B_4 = (byte)0x10;

    protected static byte B_5 = (byte)0x08;

    protected static byte B_6 = (byte)0x04;

    protected static byte B_7 = (byte)0x02;

    protected static byte B_8 = (byte)0x01;

    protected static byte[] BS = new byte[] {B_1, B_2, B_3, B_4, B_5, B_6, B_7, B_8};

    private BitSet bs = null;

    private int index;

    public ISOBitMap(Integer length) {
        bs = new BitSet(length);
    }

    public ISOBitMap(byte[] values) {
        if (values.length == 8) {
            bs = new BitSet(64);
        } else if (values.length == 16) {
            bs = new BitSet(128);
        }
        for (int i = 0; i < values.length; i++) {
            byte bit = values[i];
            for (int j = 0; j <= 7; j++) {
                int k = i * 8 + j + 1;
                if ((bit & BS[j]) == 0) {
                    bs.set(k, false);
                } else {
                    bs.set(k, true);
                }
            }
        }
    }

    public ISOBitMap(int index, BitSet bs) {
        this.index = index;
        this.bs = bs;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void dump(OutputStream os) throws ISOX8583Exception {
        try {
            os.write(dump());
        } catch (IOException e) {
            throw new ISOX8583Exception(e);
        }
    }

    @Override
    public void dump(Writer writer, String charset) throws ISOX8583Exception {
        String str = null;
        try {
            str = new String(this.dump(), charset);
        } catch (UnsupportedEncodingException e) {
            str = new String(this.dump());
        }
        try {
            writer.write(str);
        } catch (IOException e) {
            throw new ISOX8583Exception(e);
        }
    }

    @Override
    public byte[] dump() throws ISOX8583Exception {
        int length = 8;
        if (bs.get(1)) {
            length = 16;
        }
        byte[] values = new byte[length];
        for (int i = 0; i < length; i++) {
            values[i] = (byte)0x00;
            for (int j = 1; j <= 8; j++) {
                int k = i * 8 + j;
                if (bs.get(k)) {
                    int index = j - 1;
                    values[i] = (byte)(values[i] | BS[index]);
                }
            }
        }
        return values;
    }

    public boolean isExsits(int index) {
        return bs.get(index);
    }

    @Override
    public byte[] getValue() {
        return this.dump();
    }
}

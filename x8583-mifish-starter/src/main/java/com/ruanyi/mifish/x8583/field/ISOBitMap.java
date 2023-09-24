package com.ruanyi.mifish.x8583.field;

import java.util.BitSet;

import com.ruanyi.mifish.x8583.ISOField;

/**
 * 
 * ISOBitMap
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-2
 */
public class ISOBitMap implements ISOField {

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

    public ISOBitMap(int index, byte[] values) {
        this.index = index;
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

    /**
     * isExsits
     * 
     * @param index
     * @return
     */
    public boolean isExsits(int index) {
        return bs.get(index);
    }

    /**
     * getValuesLength
     * 
     * @return
     */
    public int getValuesLength() {
        byte[] data = this.bs.toByteArray();
        return data.length;
    }

    /**
     * getValue
     * 
     * @return
     */
    @Override
    public byte[] getValues() {
        return this.bs.toByteArray();
    }
}

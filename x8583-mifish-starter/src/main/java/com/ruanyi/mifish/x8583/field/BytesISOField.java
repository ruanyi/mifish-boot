package com.ruanyi.mifish.x8583.field;

import com.ruanyi.mifish.x8583.ISOField;

/**
 * 
 * BasicISOField
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-2
 */
public class BytesISOField implements ISOField {

    private byte[] value;

    private int index;

    public BytesISOField(int index, byte[] value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public byte[] getValues() {
        return value;
    }

}

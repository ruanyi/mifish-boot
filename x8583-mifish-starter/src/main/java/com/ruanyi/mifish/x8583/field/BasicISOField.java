package com.ruanyi.mifish.x8583.field;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.ruanyi.mifish.x8583.ISOField;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;

/**
 * 
 * BasicISOField
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-2
 */
public class BasicISOField implements ISOField {

    private byte[] value;

    private int index;

    public BasicISOField(int index, byte[] value) {
        this.index = index;
        this.value = value;
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
    public byte[] getValue() {
        return value;
    }

    @Override
    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public void dump(OutputStream os) {
        try {
            os.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dump(Writer writer, String charset) throws ISOX8583Exception {
        try {
            writer.write(new String(value, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] dump() throws ISOX8583Exception {
        return value;
    }
}

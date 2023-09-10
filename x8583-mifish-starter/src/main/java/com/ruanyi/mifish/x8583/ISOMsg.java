package com.ruanyi.mifish.x8583;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;

/**
 * 
 * SimpleISOMsg
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public class ISOMsg implements ISOComponent {

    protected Map<Integer, ISOComponent> fields = new java.util.TreeMap<Integer, ISOComponent>();

    protected int index = -1;

    public ISOMsg() {

    }

    public ISOMsg(int index) {
        this.index = index;
    }

    public void setField(ISOComponent component) {
        this.fields.put(component.getIndex(), component);
    }

    public ISOComponent getField(int index) {
        return this.fields.get(index);
    }

    public void setField(int index, ISOComponent component) {
        component.setIndex(index);
        this.fields.put(index, component);
    }

    public void dump(OutputStream os) throws ISOX8583Exception {
        try {
            os.write(dump());
        } catch (IOException e) {
            throw new ISOX8583Exception(e);
        }
    }

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

    public byte[] dump() throws ISOX8583Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            for (Iterator<Integer> itr = this.fields.keySet().iterator(); itr.hasNext();) {
                int index = itr.next();
                os.write((String.valueOf(index) + ":").getBytes());
                os.write(this.fields.get(index).dump());
                os.write("\r\n".getBytes());
            }
            //
            return os.toByteArray();
        } catch (IOException e) {
            throw new ISOX8583Exception(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new ISOX8583Exception(e);
                }
            }
        }
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public byte[] getValue() {
        return this.dump();
    }
}

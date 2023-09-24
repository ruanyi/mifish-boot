package com.ruanyi.mifish.x8583.model;

import java.util.Iterator;
import java.util.Map;

import com.ruanyi.mifish.x8583.ISOField;

/**
 * 
 * SimpleISOMsg
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public class ISOMsg {

    protected Map<Integer, ISOField> fields = new java.util.TreeMap<>();

    public ISOMsg() {

    }

    public void setField(ISOField isoField) {
        this.fields.put(isoField.getIndex(), isoField);
    }

    public ISOField getField(int index) {
        return this.fields.get(index);
    }

    /**
     * getISOFieldIterator
     *
     * @return
     */
    public Iterator<Map.Entry<Integer, ISOField>> getISOFieldIterator() {
        return this.fields.entrySet().iterator();
    }
}

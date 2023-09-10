package com.ruanyi.mifish.x8583;

import java.io.OutputStream;
import java.io.Writer;

import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-09 22:44
 */
public interface ISOComponent {

    /**
     * getIndex
     *
     * @return
     */
    int getIndex();

    /**
     * setIndex
     *
     * @param index
     */
    void setIndex(int index);

    /**
     * dump
     *
     * @param os
     * @throws ISOX8583Exception
     */
    void dump(OutputStream os) throws ISOX8583Exception;

    /**
     * dump
     *
     * @param writer
     * @param charset
     * @throws ISOX8583Exception
     */
    void dump(Writer writer, String charset) throws ISOX8583Exception;

    /**
     * dump
     *
     * @return
     * @throws ISOX8583Exception
     */
    byte[] dump() throws ISOX8583Exception;

    /**
     * getValue
     * 
     * @return
     */
    byte[] getValue();
}

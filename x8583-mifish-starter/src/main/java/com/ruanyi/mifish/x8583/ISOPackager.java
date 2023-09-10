package com.ruanyi.mifish.x8583;

import java.util.Map;

import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-09 22:42
 */
public interface ISOPackager {

    /**
     * getType
     *
     * @param index
     * @return
     */
    ISOType getType(int index);

    /**
     * pack
     *
     * @param msg
     * @return
     * @throws ISOX8583Exception
     */
    byte[] pack(ISOMsg msg) throws ISOX8583Exception;

    /**
     * unpack
     *
     * @param bytes
     * @return
     * @throws ISOX8583Exception
     */
    Map<Integer, ISOComponent> unpack(byte[] bytes) throws ISOX8583Exception;

    /**
     * getCharset
     *
     * @return
     */
    String getCharset();
}

package com.ruanyi.mifish.x8583;

import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-09 22:42
 */
public interface ISOType {

    void setLength(int length);

    int getLength();

    byte[] pack(ISOComponent component) throws ISOX8583Exception;

    ISOComponent unpack(Integer index, byte[] values) throws ISOX8583Exception;

    byte[] read(byte[] msg);

    byte[] take(byte[] msg);

    void setCharset(String charset);

    String getCharset();
}

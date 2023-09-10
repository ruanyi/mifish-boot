package com.ruanyi.mifish.x8583.packager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.ruanyi.mifish.x8583.ISOComponent;
import com.ruanyi.mifish.x8583.ISOMsg;
import com.ruanyi.mifish.x8583.ISOPackager;
import com.ruanyi.mifish.x8583.ISOType;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.ISOBitMap;
import com.ruanyi.mifish.x8583.types.IF_BitMap;

/**
 * 
 * SimpleISOConfigure
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public class SimpleISOPackager implements ISOPackager {

    protected Map<Integer, ISOType> types = new HashMap<>();

    protected Charset charset = Charset.forName("UTF-8");

    /**
     * SimpleISOPackager
     *
     * @param types
     */
    public SimpleISOPackager(Map<Integer, ISOType> types) {
        this.types.putAll(types);
    }

    @Override
    public ISOType getType(int index) {
        return this.types.get(index);
    }

    @Override
    public byte[] pack(ISOMsg msg) throws ISOX8583Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream(2048);
        try {
            ISOType mti = this.types.get(0);
            if (mti == null) {

            } else {
                ISOComponent comp = msg.getField(0);
                os.write(mti.pack(comp));
            }

            ISOBitMap bitmap = (ISOBitMap)msg.getField(1);
            os.write(bitmap.dump());

            IF_BitMap bmType = (IF_BitMap)this.types.get(1);

            for (int index = 2; index <= bmType.getLength() * 8; index++) {
                if (!bitmap.isExsits(index)) {
                    continue;
                }
                ISOComponent comp = msg.getField(index);
                ISOType type = this.types.get(index);
                byte[] value = type.pack(comp);
                os.write(value);
            }

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
    public Map<Integer, ISOComponent> unpack(byte[] bytes) throws ISOX8583Exception {
        Map<Integer, ISOComponent> component = new TreeMap<>();
        ISOType mti = this.types.get(0);
        // 解读mti
        if (mti == null) {
        } else {
            ISOComponent cp = mti.unpack(0, mti.read(bytes));
            component.put(0, cp);
            bytes = mti.take(bytes);
        }
        // 解包
        IF_BitMap bitmap = (IF_BitMap)this.types.get(1);
        byte[] value = bitmap.read(bytes);
        ISOBitMap bm = (ISOBitMap)bitmap.unpack(1, value);
        bytes = bitmap.take(bytes);
        for (int i = 2, len = bitmap.getLength() * 8; i <= len; i++) {
            if (!bm.isExsits(i)) {
                continue;
            }
            ISOType type = this.types.get(i);
            ISOComponent cp = type.unpack(i, type.read(bytes));
            component.put(i, cp);
            bytes = type.take(bytes);
            // if (bytes.length==0) {
            // break;
            // }
        }
        return component;
    }

    @Override
    public String getCharset() {
        return this.charset.name();
    }
}

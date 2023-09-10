package com.ruanyi.mifish.x8583;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.ruanyi.mifish.common.utils.ClassUtil;
import com.ruanyi.mifish.common.utils.ResourceUtil;
import com.ruanyi.mifish.common.utils.XMLUtil;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.BasicISOField;
import com.ruanyi.mifish.x8583.field.ISOBitMap;
import com.ruanyi.mifish.x8583.packager.SimpleISOPackager;

/**
 * 
 * 8583报文打包器 支持BCD码
 * 
 * @author ruanyi
 * @time:2013-06-01
 */
public final class X8583Util {

    public static final String CONFIG_PATH = "classpath:x8583_v1.xml";

    public static final String DEFAULT_CHARSET = "utf-8";

    private X8583Util() {}

    private static ISOPackager createISOPackager(InputStream is, String charset) throws ISOX8583Exception {
        try {
            Element root = XMLUtil.loadAsElement(is, "isopackager");
            List<Element> elements = XMLUtil.getChildElements(root, "isofield");
            Map<Integer, ISOType> map = new HashMap<>();
            for (Element element : elements) {
                String id = XMLUtil.getAttribute(element, "id");
                String len = XMLUtil.getAttribute(element, "length");
                String clazz = XMLUtil.getAttribute(element, "class");
                ISOType t = (ISOType)ClassUtil.instance(clazz, new Object[] {Integer.parseInt(len)});
                t.setCharset(charset);
                map.put(Integer.parseInt(id), t);
            }
            //
            SimpleISOPackager packager = new SimpleISOPackager(map);
            return packager;
        } catch (Exception e) {
            throw new ISOX8583Exception(e);
        }
    }

    public static ISOPackager createISOPackager(String configLocation) throws ISOX8583Exception {
        InputStream is = ResourceUtil.loadAsStream(configLocation);
        try {
            if (is == null) {
                throw new ISOX8583Exception(
                    "cannot locad input stream from 8583 configure location[" + configLocation + "]");
            }
            return createISOPackager(is, DEFAULT_CHARSET);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new ISOX8583Exception(e);
                }
            }
        }
    }

    public static ISOPackager createISOPackager(String configLocation, String charset) throws ISOX8583Exception {
        InputStream is = ResourceUtil.loadAsStream(configLocation);
        if (is == null) {
            throw new ISOX8583Exception(
                "cannot locad input stream from 8583 configure location[" + configLocation + "]");
        }
        return createISOPackager(is, charset);
    }

    public static Map<Integer, String> unpack(ISOPackager packager, byte[] message) throws ISOX8583Exception {
        Map<Integer, String> result = new HashMap<>();
        Map<Integer, ISOComponent> map = packager.unpack(message);
        for (Map.Entry<Integer, ISOComponent> entry : map.entrySet()) {
            Integer key = entry.getKey();
            ISOComponent c = entry.getValue();
            String str;
            try {
                str = new String(c.getValue(), packager.getCharset());
            } catch (UnsupportedEncodingException e) {
                str = new String(c.getValue());
            }
            result.put(key, str);
        }
        return result;
    }

    public static byte[] pack(ISOPackager packager, Map<Integer, String> values) throws ISOX8583Exception {
        ISOMsg msg = new ISOMsg();
        BitSet bs = null;
        boolean flag = false;
        for (Map.Entry<Integer, String> entry : values.entrySet()) {
            Integer key = entry.getKey();
            if (key > 64) {
                flag = true;
                break;
            }
        }
        //
        if (flag) {
            bs = new BitSet(128);
            bs.set(1, true);
        } else {
            bs = new BitSet(64);
            bs.set(1, false);
        }
        //
        for (Map.Entry<Integer, String> entry : values.entrySet()) {
            Integer key = entry.getKey();
            bs.set(key, true);
            String value = entry.getValue();
            ISOComponent component = null;
            try {
                component = new BasicISOField(key, value.getBytes(packager.getCharset()));
            } catch (UnsupportedEncodingException e) {
                component = new BasicISOField(key, value.getBytes());
            }
            /***/
            msg.setField(key, component);
        }
        //
        msg.setField(1, new ISOBitMap(1, bs));
        return packager.pack(msg);
    }
}

package com.ruanyi.mifish.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;

/**
 * Description:
 * <p>
 * 字符串压缩工具
 *
 * @author: ruanyi
 * @Date: 2019-09-12 16:45
 */
public final class GzipUtil {

    private static final String UTF_8_CHARSET = "UTF-8";

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /**
     * compress
     *
     * @param str
     * @return
     */
    public static String compress(String str) {
        return compress(str, UTF_8_CHARSET);
    }

    /**
     * 字符串压缩工具
     *
     * @param str
     * @param charset
     * @return
     */
    public static String compress(String str, String charset) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] datas = Base64.getEncoder().encode(str.getBytes(charset));
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(datas);
            gzip.close();
            return out.toString("ISO-8859-1");
        } catch (IOException ex) {
            LOG.warn(ex, Pair.of("clazz", "GzipUtil"), Pair.of("method", "compress"), Pair.of("str", str),
                Pair.of("charset", charset), Pair.of("gzip_compress_status", "exception"));
            return str;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 字符串压缩工具
     *
     * @param bytes
     * @return
     */
    public static byte[] compress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return bytes;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            gzip.close();
            return out.toByteArray();
        } catch (IOException ex) {
            LOG.warn(ex, Pair.of("clazz", "GzipUtil"), Pair.of("method", "compress"), Pair.of("bytes", bytes),
                Pair.of("gzip_compress_status", "exception"));
            return bytes;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * uncompress
     *
     * @param str
     * @return
     */
    public static String uncompress(String str) {
        if (isCompressed(str)) {
            return uncompress(str, UTF_8_CHARSET);
        }
        return str;
    }

    /**
     * 字符串解压缩
     *
     * @param str
     * @param charset
     * @return
     */
    public static String uncompress(String str, String charset) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();;
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset;
            while ((offset = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, offset);
            }
            ungzip.close();
            byte[] data = Base64.getDecoder().decode(out.toByteArray());
            return new String(data, charset);
        } catch (IOException ex) {
            LOG.warn(ex, Pair.of("clazz", "GzipUtil"), Pair.of("method", "uncompress"), Pair.of("str", str),
                Pair.of("charset", charset), Pair.of("gzip_uncompress_status", "exception"));
            return str;
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 字符串解压缩
     *
     * @param bytes
     * @return
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return bytes;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(bytes);
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset;
            while ((offset = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, offset);
            }
            ungzip.close();
            return out.toByteArray();
        } catch (IOException ex) {
            LOG.warn(ex, Pair.of("clazz", "GzipUtil"), Pair.of("method", "uncompress"), Pair.of("bytes", bytes),
                Pair.of("gzip_uncompress_status", "exception"));
            return bytes;
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * isCompressed
     *
     * @param str
     * @return
     */
    public static boolean isCompressed(String str) {
        try {
            if (str == null || str.isEmpty()) {
                return false;
            }
            return isCompressed(str.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            // ingore
            return isCompressed(str.getBytes());
        }
    }

    /**
     * 判断是否经过gzip压缩
     *
     * @param compressed
     * @return
     */
    public static boolean isCompressed(final byte[] compressed) {
        if (compressed == null || compressed.length < 2) {
            return false;
        }
        return (compressed[0] == (byte)(GZIPInputStream.GZIP_MAGIC))
            && (compressed[1] == (byte)(GZIPInputStream.GZIP_MAGIC >> 8));
    }
}

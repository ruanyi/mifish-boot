package com.ruanyi.mifish.kernel.model.info;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.JacksonUtils;
import com.ruanyi.mifish.kernel.model.MediaType;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-04-24 21:17
 */
public final class ImageInfo extends MetaInfo {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.media;

    /**
     * 不允许自主实例化
     *
     * 两个构造函数，只能调用其中一个 <br>
     *
     * 计算失败时，调用这个构造函数
     *
     * @param code
     * @param message
     */
    private ImageInfo(String code, String message) {
        super(code, message);
    }

    /**
     * 不允许自主实例化
     *
     * 两个构造函数，只能调用其中一个 <br>
     *
     * 计算失败时，调用这个构造函数
     *
     */
    private ImageInfo(String imageInfo) {
        super(imageInfo);
    }

    /**
     * getImageInfo
     * 
     * @return
     */
    public String getImageInfo() {
        return super.getMediaInfo();
    }

    /**
     * getHeight
     * 
     * @return
     */
    public Integer getHeight() {
        try {
            if (isEmpty()) {
                return -1;
            }
            Object v = this.metas.get("height");
            if (v instanceof Number) {
                return ((Number)v).intValue();
            }
            return Integer.parseInt(v + "");
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "ImageInfo"), Pair.of("method", "getHeight"),
                Pair.of("imageInfo", getImageInfo()), Pair.of("obtain_height_status", "exception"));
            return -1;
        }
    }

    /**
     * getWidth
     * 
     * @return
     */
    public Integer getWidth() {
        try {
            if (isEmpty()) {
                return -1;
            }
            Object v = this.metas.get("width");
            if (v instanceof Number) {
                return ((Number)v).intValue();
            }
            return Integer.parseInt(v + "");
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "ImageInfo"), Pair.of("method", "getWidth"),
                Pair.of("imageInfo", getImageInfo()), Pair.of("obtain_width_status", "exception"));
            return -1;
        }
    }

    /**
     * getOrientation
     *
     * @return
     */
    public String getOrientation() {
        try {
            if (isEmpty()) {
                return null;
            }
            Object v = this.metas.get("orientation");
            return v + "";
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "ImageInfo"), Pair.of("method", "getOrientation"),
                Pair.of("imageInfo", getImageInfo()), Pair.of("obtain_orientation", "exception"));
            return null;
        }
    }

    /**
     * getFileSize
     * 
     * @return
     */
    public String getFileSize() {
        try {
            if (isEmpty()) {
                return null;
            }
            Object v = this.metas.get("fileSize");
            return v + "";
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "ImageInfo"), Pair.of("method", "getFileSize"),
                Pair.of("imageInfo", getImageInfo()), Pair.of("obtain_filesize_status", "exception"));
            return null;
        }
    }

    /**
     * @see MetaInfo#getMediaType()
     */
    @Override
    public MediaType getMediaType() {
        return MediaType.IMAGE;
    }

    /**
     * failure
     *
     * @param errInfo
     * @return
     */
    public static ImageInfo failure(String errInfo) {
        Map<String, Object> metas = JacksonUtils.json2Map(errInfo, Object.class);
        if (metas == null || metas.isEmpty()) {
            return empty();
        }
        String code = metas.get("code") + "";
        String message = metas.get("message") + "";
        return new ImageInfo(code, message);
    }

    /**
     * empty
     * 
     * @return
     */
    public static ImageInfo empty() {
        return new ImageInfo("999", "unknow exception");
    }

    /**
     * from
     *
     * @param info
     * @return
     */
    public static ImageInfo from(String info) {
        Map<String, Object> metas = JacksonUtils.json2Map(info, Object.class);
        if (metas != null && !metas.isEmpty()) {
            ImageInfo imageInfo = new ImageInfo(info);
            imageInfo.metas.putAll(metas);
            return imageInfo;
        }
        return new ImageInfo("199", "image info解析出来为空");
    }
}

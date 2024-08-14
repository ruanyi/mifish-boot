package com.ruanyi.mifish.kernel.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 11:41
 */
public enum MediaType {

    /** 图片媒体 */
    IMAGE(1, "图片"),

    /** 视频媒体 */
    VIDEO(2, "视频"),

    /** 音频媒体 */
    AUDIO(3, "音频");

    /** code */
    private int code;

    /** desc */
    private String desc;

    /**
     * MediaType
     *
     * @param code
     * @param desc
     */
    MediaType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * getCode
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     * getDesc
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * of
     *
     * @param code
     * @return
     */
    public static MediaType of(int code) {
        for (MediaType mt : values()) {
            if (mt.getCode() == code) {
                return mt;
            }
        }
        return null;
    }

    /**
     * 根据key解析出具体类型
     *
     * @param key
     * @return
     */
    public static MediaType parse(String key) {
        if (StringUtils.isNotBlank(key)) {
            if (StringUtils.endsWithIgnoreCase(key, "mp4") || StringUtils.endsWithIgnoreCase(key, "quicktime")) {
                return MediaType.VIDEO;
            }
            if (StringUtils.endsWithIgnoreCase(key, "mp3") || StringUtils.endsWithIgnoreCase(key, "aac")
                || StringUtils.endsWithIgnoreCase(key, "wav")) {
                return MediaType.AUDIO;
            }
            if (StringUtils.endsWithIgnoreCase(key, "jpg") || StringUtils.endsWithIgnoreCase(key, "png")
                || StringUtils.endsWithIgnoreCase(key, "jpeg") || StringUtils.endsWithIgnoreCase(key, "webp")
                || StringUtils.endsWithIgnoreCase(key, "heic") || StringUtils.endsWithIgnoreCase(key, "gif")) {
                return MediaType.IMAGE;
            }
        }
        return null;
    }

    /**
     * parseByMime
     *
     * @param mime
     * @return
     */
    public static MediaType parseByMime(String mime) {
        if (StringUtils.isEmpty(mime)) {
            return null;
        }
        if (mime.contains("video/")) {
            return MediaType.VIDEO;
        } else if (mime.contains("image/")) {
            return MediaType.IMAGE;
        } else if (mime.contains("audio/")) {
            return MediaType.AUDIO;
        }
        return null;
    }
}

package com.ruanyi.mifish.kernel.model.info;

import java.util.HashMap;
import java.util.Map;

import com.ruanyi.mifish.kernel.model.MediaType;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2021-07-13 13:38
 */
public abstract class MetaInfo {

    /** 计算失败时，有值，返回具体的：status */
    private String code;

    /**
     * 计算失败时，有值，返回具体的：message
     */
    private String message;

    /**
     * 计算成功时，有值：mediaInfo
     */
    private String mediaInfo;

    /**
     * 存储图片的元数据信息
     */
    protected Map<String, Object> metas = new HashMap<>();

    /**
     * 两个构造函数，只能调用其中一个
     * <p>
     * 计算失败时，调用这个构造函数
     *
     * @param code
     * @param message
     */
    protected MetaInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 两个构造函数，只能调用其中一个
     * <p>
     * 计算成功时，调用这个构造函数
     *
     * @param mediaInfo
     */
    protected MetaInfo(String mediaInfo) {
        this.mediaInfo = mediaInfo;
    }

    /**
     * toMap
     *
     * @return
     */
    public Map<String, Object> toMap() {
        return new HashMap<>(this.metas);
    }

    /**
     * isEmpty
     *
     * @return
     */
    public boolean isEmpty() {
        return (this.metas == null || this.metas.isEmpty());
    }

    /**
     * getCode
     * 
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * getMessage
     * 
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * getMediaInfo
     *
     * @return
     */
    public String getMediaInfo() {
        return mediaInfo;
    }

    /**
     * getMediaType
     *
     * @return
     */
    public abstract MediaType getMediaType();
}

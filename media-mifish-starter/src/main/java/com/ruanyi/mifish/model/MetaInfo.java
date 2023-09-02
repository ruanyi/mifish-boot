package com.ruanyi.mifish.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2021-07-13 13:38
 */
public abstract class MetaInfo {

    /**
     * 计算失败时，有值，返回具体的：status
     */
    private Integer status;

    /**
     * 计算失败时，有值，返回具体的：desc
     */
    private String desc;

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
     * @param status
     * @param desc
     */
    protected MetaInfo(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
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
     * getStatus
     *
     * @return
     */
    public Integer getStatus() {
        return status;
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

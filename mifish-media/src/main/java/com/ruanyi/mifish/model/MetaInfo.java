package com.ruanyi.mifish.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.GroovyUtil;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2021-07-13 13:38
 */
public abstract class MetaInfo {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.meida;

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
    MetaInfo(Integer status, String desc) {
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
    MetaInfo(String mediaInfo) {
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
     * getMeta
     *
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getMeta(String path, Class<T> clazz) {
        try {
            Object value = GroovyUtil.executeScript(path, this.metas);
            if (value != null) {
                return clazz.cast(value);
            }
            return null;
        } catch (Exception e) {
            LOG.error(e, Pair.of("clazz", "MetaInfo"), Pair.of("method", "getMeta"), Pair.of("path", path),
                Pair.of("mediaInfo", this.mediaInfo), Pair.of("obtain_media_status", "exception"));
            return null;
        }
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

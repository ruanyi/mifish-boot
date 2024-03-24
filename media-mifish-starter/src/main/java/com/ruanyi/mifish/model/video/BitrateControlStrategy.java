package com.ruanyi.mifish.model.video;

/**
 * Description:
 *
 * 码率控制策略
 *
 * @author: ruanyi
 * @Date: 2023-09-03 18:20
 */
public enum BitrateControlStrategy {

    /**
     * 根据原始视频的 width * height * fps
     *
     * 目前的默认策略
     */
    W_H_FPS(0, "根据width*height * fps"),

    /**
     * 原始视频的倍数
     *
     * 默认值：1倍，也就是原始视频的码率
     */
    SOURCE_VIDEO_MULTIPLE(1, "原始视频的倍数"),

    /**
     * 根据分辨率的经验值
     *
     * 480p: 2500Kbps <br/>
     * 540p: 2500Kbps <br/>
     * 720p: 3000Kbps <br/>
     * 1080p: 5000Kbps <br/>
     * 2k:10000 Kbps <br/>
     * 4k: 20000 Kbps
     */
    RESOLUTION_EXPERIENCE(2, "根据分辨率的经验值"),

    /**
     * 自定义值，由业务方直接传入
     */
    CUSTOM_VALUE(9, "自定义值"),;

    /** code */
    private int code;

    /** desc */
    private String desc;

    /**
     * BitrateControlStrategy
     * 
     * @param code
     * @param desc
     */
    BitrateControlStrategy(int code, String desc) {
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
    public static BitrateControlStrategy of(Integer code) {
        if (code != null) {
            for (BitrateControlStrategy strategy : values()) {
                if (strategy.code == code) {
                    return strategy;
                }
            }
        }
        return null;
    }
}

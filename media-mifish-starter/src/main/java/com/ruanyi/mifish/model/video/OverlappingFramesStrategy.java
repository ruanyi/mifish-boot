package com.ruanyi.mifish.model.video;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-08-21 20:40
 */
public enum OverlappingFramesStrategy {

    /**
     * 没有重叠帧，正常、精确切片
     */
    PRECISE(0, "精确切片"),

    /**
     * 片与片之间，前向重叠帧
     */
    PRE(1, "前向重叠帧"),

    /** 片与片之间，后置重叠帧 */
    POST(2, "后置重叠帧"),

    /**
     * 片与片之间，双向重叠帧
     */
    TWO_WAY(3, "双向重叠帧");

    /** code */
    private int code;

    /** desc */
    private String desc;

    /**
     * SlicingStrategy
     *
     * @param code
     * @param desc
     */
    OverlappingFramesStrategy(int code, String desc) {
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
    public static OverlappingFramesStrategy of(Integer code) {
        if (code != null) {
            for (OverlappingFramesStrategy strategy : values()) {
                if (strategy.code == code) {
                    return strategy;
                }
            }
        }
        return null;
    }
}

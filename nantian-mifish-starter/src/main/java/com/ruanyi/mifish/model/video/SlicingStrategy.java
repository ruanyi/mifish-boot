package com.ruanyi.mifish.model.video;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 20:40
 */
public enum SlicingStrategy {

    /** 按时长切片 */
    DURATION(1, "按时长切片"),

    /** 按关键帧切片 */
    KEY_FRAMES(2, "按关键帧切片"),

    /** 稀疏帧切片 */
    SPARSE_FRAMES(3, "稀疏帧切片");

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
    SlicingStrategy(int code, String desc) {
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
    public static SlicingStrategy of(int code) {
        for (SlicingStrategy strategy : values()) {
            if (strategy.code == code) {
                return strategy;
            }
        }
        return null;
    }
}

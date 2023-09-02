package com.ruanyi.mifish.model.video;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-02 20:40
 */
public enum SlicingStrategy {

    /** 片与片之间，会有重叠帧 */
    OVERLAPPING_FRAMES(1, "重叠帧"),

    /** 没有多余帧的概念，正常切片 */
    NORMAL_FRAMES(2, "正常切片"),

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

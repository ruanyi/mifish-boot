package com.ruanyi.mifish.model.video;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-02-26 20:05
 */
public enum SegmentPtsStrategy {

    /** 所有切片的PTS归0 */
    MAKE_ZERO(1, "切片pts归0"),

    /** 默认策略 */
    MAINTAIN(0, "维持原始视频的PTS");

    /** code */
    private int code;

    /**
     * desc
     */
    private String desc;

    /**
     * PtsResetStrategy
     *
     * @param code
     * @param desc
     */
    SegmentPtsStrategy(int code, String desc) {
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
    public static SegmentPtsStrategy of(Integer code) {
        if (code == null) {
            return SegmentPtsStrategy.MAINTAIN;
        }
        for (SegmentPtsStrategy ptsStrategy : values()) {
            if (ptsStrategy.code == code) {
                return ptsStrategy;
            }
        }
        return SegmentPtsStrategy.MAINTAIN;
    }
}

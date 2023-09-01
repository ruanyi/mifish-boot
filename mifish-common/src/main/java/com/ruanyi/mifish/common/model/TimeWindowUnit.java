package com.ruanyi.mifish.common.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2017-12-18 23:15
 */
public enum TimeWindowUnit {

    SECOND(1000, "s"),

    MINUTE(60 * SECOND.getMillisecond(), "m"),

    HOUR(60 * MINUTE.getMillisecond(), "h"),

    DAY(24 * HOUR.getMillisecond(), "d");

    /**
     * millisecond
     * <p>
     * 毫秒数，
     */
    private final long millisecond;

    /**
     * unit
     * <p>
     * 单位：忽略大小写
     */
    private final String unit;

    /**
     * TimeWindowUnit
     *
     * @param millisecond
     * @param unit
     */
    TimeWindowUnit(long millisecond, String unit) {
        this.millisecond = millisecond;
        this.unit = unit;
    }

    /**
     * getMillisecond
     *
     * @return
     */
    public long getMillisecond() {
        return millisecond;
    }

    /**
     * getUnit
     *
     * @return
     */
    public String getUnit() {
        return unit;
    }

    /**
     * getTimeWindowUnit
     *
     * @param unit
     * @return
     */
    public static TimeWindowUnit getTimeWindowUnit(String unit) {
        for (TimeWindowUnit twu : values()) {
            if (StringUtils.equalsIgnoreCase(twu.getUnit(), unit)) {
                return twu;
            }
        }
        return null;
    }
}

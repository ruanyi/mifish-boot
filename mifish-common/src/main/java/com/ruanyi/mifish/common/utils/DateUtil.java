package com.ruanyi.mifish.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2017-12-04 18:33
 */
public final class DateUtil {

    public static final String[] WEEK_DAY_OF_CHINESE = new String[] {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    /**
     * 毫秒
     */
    public final static long MS = 1;
    /**
     * 每秒钟的毫秒数
     */
    public final static long SECOND_MS = MS * 1000;
    /**
     * 每分钟的毫秒数
     */
    public final static long MINUTE_MS = SECOND_MS * 60;
    /**
     * 每小时的毫秒数
     */
    public final static long HOUR_MS = MINUTE_MS * 60;
    /**
     * 每天的毫秒数
     */
    public final static long DAY_MS = HOUR_MS * 24;

    /**
     * 标准日期格式
     */
    public final static String NORM_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 标准短日期格式
     */
    public static final String NORM_SIMPLE_DATE_PATTERN = "yyyyMMdd";

    /**
     * 标准时间格式
     */
    public final static String NORM_TIME_PATTERN = "HH:mm:ss";

    /**
     * 标准日期时间格式
     */
    public final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * NORM_DATETIME_FORMAT
     */
    public static final FastDateFormat NORM_DATETIME_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_PATTERN);

    /**
     * NORM_DATE_FORMAT
     */
    public static final FastDateFormat NORM_DATE_FORMAT = FastDateFormat.getInstance(NORM_DATE_PATTERN);

    /**
     * dateToString
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        try {
            if (date == null) {
                return "";
            }
            return NORM_DATETIME_FORMAT.format(date);
        } catch (Exception ex) {
            MifishLogs.framework.error(ex, Pair.of("clazz", "DateUtil"), Pair.of("method", "stringToDate"),
                Pair.of("date", date), Pair.of("pattern", NORM_DATETIME_PATTERN), Pair.of("status", "exceptions"));
            return "";
        }
    }

    /**
     * 常用于处理日志格式
     *
     * @param time
     * @return
     */
    public static String longTime2String(long time) {
        Date date = new Date(time);
        return dateToString(date);
    }

    /**
     * dateToString
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        try {
            if (date == null) {
                return "";
            }
            FastDateFormat fdf = FastDateFormat.getInstance(pattern);
            return fdf.format(date);
        } catch (Exception ex) {
            MifishLogs.framework.error(ex, Pair.of("clazz", "DateUtil"), Pair.of("method", "stringToDate"),
                Pair.of("date", date), Pair.of("pattern", pattern), Pair.of("status", "exceptions"));
            return "";
        }
    }

    /**
     * @param dateString
     * @return
     * @throws Exception
     */
    public static Date stringToDate(String dateString) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        int len = StringUtils.length(dateString);
        if (len == 10) {
            return stringToDate(dateString, NORM_DATE_PATTERN);
        } else if (len == 8) {
            return stringToDate(dateString, NORM_SIMPLE_DATE_PATTERN);
        }
        return stringToDate(dateString, NORM_DATETIME_PATTERN);
    }

    /**
     * stringToDate
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date stringToDate(String dateString, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(dateString);
        } catch (ParseException e) {
            MifishLogs.framework.error(e, Pair.of("clazz", "DateUtil"), Pair.of("method", "stringToDate"),
                Pair.of("dateString", dateString), Pair.of("pattern", pattern), Pair.of("status", "exceptions"));
            return null;
        }
    }

    /**
     * todayString
     *
     * @return
     */
    public static String todayString() {
        return dateToString(new Date(), NORM_SIMPLE_DATE_PATTERN);
    }

    /**
     * 昨天
     *
     * @return 昨天
     */
    public static Date yesterday() {
        return offsiteDate(new Date(), Calendar.DAY_OF_YEAR, -1);
    }

    /**
     * 上周
     *
     * @return 上周
     */
    public static Date lastWeek() {
        return offsiteDate(new Date(), Calendar.WEEK_OF_YEAR, -1);
    }

    /**
     * 上个月
     *
     * @return 上个月
     */
    public static Date lastMouth() {
        return offsiteDate(new Date(), Calendar.MONTH, -1);
    }

    /**
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date 基准日期
     * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
     * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
     * @return 偏移后的日期
     */
    private static Date offsiteDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }

    /**
     * 将时间转成：每周几
     * 
     * @param date
     * @return
     */
    public static String dateToWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return WEEK_DAY_OF_CHINESE[cal.get(WEEK_DAY_OF_CHINESE.length) - 1];
    }

    public static LocalDateTime addSecond(LocalDateTime date, int second) {
        return date.plusSeconds(second);
    }

    public static LocalDateTime addMinute(LocalDateTime date, int minute) {
        return date.plusMinutes(minute);
    }

    public static LocalDateTime addHour(LocalDateTime date, int hour) {
        return date.plusHours(hour);
    }

    public static LocalDateTime addDay(LocalDateTime date, int day) {
        return date.plusDays(day);
    }

    public static LocalDateTime addMonth(LocalDateTime date, int month) {
        return date.plusMonths(month);
    }

    public static LocalDateTime addYear(LocalDateTime date, int year) {
        return date.plusYears(year);
    }

    /**
     * 获取当前的小时
     *
     * @return
     */
    public static int getCurrentHour() {
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        return currentHour;
    }

    private DateUtil() {

    }
}

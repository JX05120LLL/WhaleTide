package com.whaletide.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    /**
     * 日期格式常量
     */
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATETIME_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";
    public static final String PATTERN_DATE_CHINESE = "yyyy年MM月dd日";
    
    /**
     * 获取当前日期时间字符串，格式：yyyy-MM-dd HH:mm:ss
     * @return 当前日期时间字符串
     */
    public static String getCurrentDateTime() {
        return formatDateTime(LocalDateTime.now());
    }
    
    /**
     * 获取当前日期字符串，格式：yyyy-MM-dd
     * @return 当前日期字符串
     */
    public static String getCurrentDate() {
        return formatDate(LocalDate.now());
    }
    
    /**
     * 格式化LocalDateTime为字符串，格式：yyyy-MM-dd HH:mm:ss
     * @param dateTime 日期时间
     * @return 格式化后的字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(PATTERN_DATETIME));
    }
    
    /**
     * 格式化LocalDate为字符串，格式：yyyy-MM-dd
     * @param date 日期
     * @return 格式化后的字符串
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(PATTERN_DATE));
    }
    
    /**
     * 格式化LocalDateTime为指定格式的字符串
     * @param dateTime 日期时间
     * @param pattern 格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * 格式化Date为指定格式的字符串
     * @param date 日期
     * @param pattern 格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 解析字符串为LocalDateTime
     * @param dateTimeStr 日期时间字符串
     * @param pattern 格式
     * @return 解析后的LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        if (StringUtil.isEmpty(dateTimeStr)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 解析字符串为LocalDate
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 解析后的LocalDate
     */
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        if (StringUtil.isEmpty(dateStr)) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 解析字符串为Date
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 解析后的Date
     */
    public static Date parseDate(String dateStr, String pattern) {
        if (StringUtil.isEmpty(dateStr)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 计算两个日期之间的天数差
     * @param start 开始日期
     * @param end 结束日期
     * @return 天数差
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
    
    /**
     * 计算两个时间之间的小时差
     * @param start 开始时间
     * @param end 结束时间
     * @return 小时差
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.HOURS.between(start, end);
    }
    
    /**
     * 计算两个时间之间的分钟差
     * @param start 开始时间
     * @param end 结束时间
     * @return 分钟差
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }
    
    /**
     * 计算两个时间之间的秒数差
     * @param start 开始时间
     * @param end 结束时间
     * @return 秒数差
     */
    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.SECONDS.between(start, end);
    }
    
    /**
     * 增加天数
     * @param date 日期
     * @param days 天数
     * @return 增加后的日期
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        return date.plusDays(days);
    }
    
    /**
     * 增加小时
     * @param dateTime 时间
     * @param hours 小时数
     * @return 增加后的时间
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime.plusHours(hours);
    }
    
    /**
     * 增加分钟
     * @param dateTime 时间
     * @param minutes 分钟数
     * @return 增加后的时间
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.plusMinutes(minutes);
    }
    
    /**
     * 增加秒数
     * @param dateTime 时间
     * @param seconds 秒数
     * @return 增加后的时间
     */
    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime.plusSeconds(seconds);
    }
    
    /**
     * 减少天数
     * @param date 日期
     * @param days 天数
     * @return 减少后的日期
     */
    public static LocalDate minusDays(LocalDate date, long days) {
        return date.minusDays(days);
    }
    
    /**
     * 减少小时
     * @param dateTime 时间
     * @param hours 小时数
     * @return 减少后的时间
     */
    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        return dateTime.minusHours(hours);
    }
    
    /**
     * 减少分钟
     * @param dateTime 时间
     * @param minutes 分钟数
     * @return 减少后的时间
     */
    public static LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.minusMinutes(minutes);
    }
    
    /**
     * 判断日期是否是今天
     * @param date 日期
     * @return 是否是今天
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }
    
    /**
     * 判断日期是否是将来日期
     * @param date 日期
     * @return 是否是将来日期
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
    
    /**
     * 判断日期是否是过去日期
     * @param date 日期
     * @return 是否是过去日期
     */
    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
    
    /**
     * 将LocalDateTime转换为Date
     * @param localDateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 将LocalDate转换为Date
     * @param localDate LocalDate
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 将Date转换为LocalDateTime
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    /**
     * 将Date转换为LocalDate
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    /**
     * 获取指定日期的开始时间（00:00:00）
     * @param date 日期
     * @return 开始时间
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }
    
    /**
     * 获取指定日期的结束时间（23:59:59.999999999）
     * @param date 日期
     * @return 结束时间
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(23, 59, 59, 999999999);
    }
    
    /**
     * 获取当前时间戳（毫秒）
     * @return 时间戳
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    /**
     * 获取当前时间戳（秒）
     * @return 时间戳
     */
    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }
}

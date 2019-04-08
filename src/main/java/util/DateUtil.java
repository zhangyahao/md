package util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @program: amos-cirs
 * @description: 时间工具类 支持java8以上
 * @author: Zhang
 * @create: 2018-06-19 13:49
 **/
public class DateUtil {
    private static String LOCALDATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static String DATE_FORMAT_DATETIME = "yyyy-MM-dd";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCALDATE_FORMAT_DATETIME);
    private static DateTimeFormatter Dateformatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME);
    private static String LOCALTIME_FORMAT_DATETIME = "HH:mm:ss";

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static String getSysDateTimeString() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(formatter);
    }

    public static String getLocaTime() {
        LocalTime localTime = LocalTime.now();
        return localTime.format(DateTimeFormatter.ofPattern(LOCALTIME_FORMAT_DATETIME));
    }

    /**
     * 获取当前string 转换为日期
     */
    public static LocalDate getDate(String s) {
        return LocalDate.parse(s, formatter);
    }

    /**
     * string转换为时间
     */
    public static LocalDateTime stringGetTime(String s) {
        LocalDateTime time = LocalDateTime.parse(s, formatter);
        return time;
    }

    /**
     * 获取时间段
     *
     * @return
     */
    public static String getPeriod() {
        LocalDateTime start, end;
        LocalDateTime localDateTime = LocalDateTime.now();
        int min = localDateTime.getMinute();
        int secode = localDateTime.getSecond();
        if (min % 15 > 7) {
            //取下一个时间段
            start = localDateTime.plus(15 - (min % 15), ChronoUnit.MINUTES);
            start = start.minus(secode, ChronoUnit.SECONDS);
            end = localDateTime.plus((15 - (min % 15)) + 15, ChronoUnit.MINUTES);
            end = end.minus(secode, ChronoUnit.SECONDS);
        } else {
            start = localDateTime.minus((min % 15), ChronoUnit.MINUTES);
            start = start.minus(secode, ChronoUnit.SECONDS);
            end = localDateTime.plus(15 - (min % 15), ChronoUnit.MINUTES);
            end = end.minus(secode, ChronoUnit.SECONDS);
        }
//        return start.toString() + "_" + end.toString();
        long startTime = start.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long endTime = end.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return startTime + "_" + endTime;
    }

    /**
     * 时间计算
     *
     * @param dateA
     * @return
     */
    public static long getBetweenDay(String dateA) {
        long time1 = 0, time2 = 0;
        LocalDateTime start = stringGetTime(dateA);
        LocalDateTime end = LocalDateTime.now();
        time1 = start.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        time2 = end.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long betweenDays;
        betweenDays = (time1 - time2) / (1000 * 60);
        return betweenDays;
    }

    /**
     * 计算当前时间到晚上0点的剩余时间
     */

    public static int getRedisExpir() {
        LocalDate localDate = LocalDate.now();
        long time1, time2;
        LocalDateTime now = LocalDateTime.now();
        //第二天 0点
        String endTime = localDate.plus(1, ChronoUnit.DAYS).format(Dateformatter) + " 00:00:00";
        LocalDateTime newDay = stringGetTime(endTime);
        time1 = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        time2 = newDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long betweenDays = (time2 - time1) / 1000;
        return Math.toIntExact(betweenDays);
    }
    /**
     * 获取epg时间段 0点到当前时间
     *
     * @return
     */
    public static String getEpgPeriod() {
        LocalDateTime localDate = LocalDateTime.now();
        LocalDateTime start = localDate.minus(1, ChronoUnit.HOURS);
        long startTime = start.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long endTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return startTime + "_" + endTime;
    }

    /**
     * 将大数据给的数据转为string取redis的key
     *
     * @param timestamp
     * @return
     */
    public static String getDateTimeOfTimestamp(String timestamp) {
        Instant instant = Instant.ofEpochMilli(Long.valueOf(timestamp.substring(0, (timestamp.length() - 2))));
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 将 yyyy-MM-dd HH:mm:ss 转为yyyyMMddHHmmss
     */
    public static String conversionTime(String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 将 yyyyMMddHHmmss转为yyyy-MM-dd HH:mm:ss
     */
    public static String conversionTime2(String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return localDateTime.format(formatter);
    }

    /**
     * 将时间转换为13位毫秒级时间
     */
    public long getTimestamp(String time) {
        LocalDateTime localDateTime = stringGetTime(time);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
    /**
     * 计算日期{@code startDate}与{@code endDate}的间隔天数
     *
     * @param startDate
     * @param endDate
     * @return 间隔天数
     */
  public   static long until(LocalDate startDate, LocalDate endDate){
//        return startDate.until(endDate, ChronoUnit.DAYS);
        //或者
      return ChronoUnit.DAYS.between(startDate, endDate);
    }





}

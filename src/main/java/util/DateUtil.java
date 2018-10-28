package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @description: 时间工具类 支持java8以上
 * @author: Zhang
 * @create: 2018-06-19 13:49
 **/
public class DateUtil {
    private static LocalDateTime localDateTime  = LocalDateTime.now();
    private static String LOCALDATE_FORMAT_DATETIME = "yyyy/MM/dd HH:mm:ss";
    private static String DATE_FORMAT_DATETIME = "yyyy-MM-dd";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCALDATE_FORMAT_DATETIME);
    private static DateTimeFormatter Dateformatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME);
    private static LocalDate localDate = LocalDate.now();


    /**
     * 获取当前时间戳
     * @return
     */
    public static String getSysDateTimeString() {
        return localDateTime.format(formatter);
    }

    /**
     * 获取当前string 转换为日期
     */
    public static LocalDate getDate(String s) {
        return LocalDate.parse(s,formatter);
    }
    /**
     * string转换为时间
     */
    public static LocalDateTime stringGetTime(String s) {
        LocalDateTime time = LocalDateTime.parse(s,formatter);
        return time;
    }

    /**
     *获取时间段
     * @return
     */
    public static String getPeriod() {
        LocalDateTime start, end;
        LocalDateTime localDateTime = LocalDateTime.now();
        int min =localDateTime.getMinute();
        int secode = localDateTime.getSecond();
        if (min % 15 > 7) {
            //取下一个时间段
            start = localDateTime.plus(15-(min % 15), ChronoUnit.MINUTES);
            start = start.minus(secode, ChronoUnit.SECONDS);
            end = localDateTime.plus((15-(min % 15) )+ 15, ChronoUnit.MINUTES);
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
        return startTime+ "_" + endTime;
    }


    /**
     * 时间计算
     * @param dateA
     * @param dateB
     * @return
     */
    public static String getBetweenDay(String dateA, String dateB) {
        long time1 = 0, time2 = 0;
        LocalDateTime start = stringGetTime(dateA);
        LocalDateTime end = stringGetTime(dateB);
        time1 = start.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        time2 = end.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long betweenDays = (time2 - time1) / (1000 * 60);
        return String.valueOf(betweenDays);
    }

    /**
     * 将时间转换为13位毫秒级时间
     */
    public long getTimestamp(String time) {
        LocalDateTime localDateTime = stringGetTime(time);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 计算当前时间到晚上0点剩余时间
     */

    public static int getRedisExpir() {
        long time1, time2;
        LocalDateTime now = LocalDateTime.now();
        //第二天 0点
        String endTime = localDate.plus(1, ChronoUnit.DAYS).format(Dateformatter) + " 00:00:00";
        LocalDateTime newDay = stringGetTime(endTime);
        time1 = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        time2 = newDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long betweenDays = (time2 - time1) / 1000 ;
        return Math.toIntExact(betweenDays);
    }


}
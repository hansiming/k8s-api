package com.jd.util;

import io.airlift.units.Duration;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by guoliming on 2015/11/4.
 */
public class DateUtil {
    private static DateTimeFormatter scheduleFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").withLocale(Locale.ENGLISH);
    private static DateTimeFormatter scheduleDateFormat = DateTimeFormat.forPattern("MM/dd/yyyy").withLocale(Locale.ENGLISH);
    private static DateTimeFormatter scheduleTimeFormat = DateTimeFormat.forPattern("hh,mm,aa,z").withLocale(Locale.ENGLISH);
    public static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH);
    private static DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter javaDateFormat = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss z yyyy").withLocale(Locale.ENGLISH);

    public static String getCurrentDate() {
        return format.print(System.currentTimeMillis());
    }

    public static String getDate(String dateTimeStr) {
        DateTime dateTime = scheduleFormat.parseDateTime(dateTimeStr);
        return dateTime.toString(scheduleDateFormat);
    }

    public static String getTime(String dateTimeStr) {
        DateTime dateTime = scheduleFormat.parseDateTime(dateTimeStr);
        return dateTime.toString(scheduleTimeFormat).replace("AM", "am").replace("PM", "pm");
    }

    public static DateTime parseDate(String dateStr) {
        return dateFormat.parseDateTime(dateStr);
    }

    public static DateTime getDateTime(String dateTimeStr) {
        return scheduleFormat.parseDateTime(dateTimeStr);
    }

    public static DateTime parseJavaDate(String dateTimeStr) {
        return javaDateFormat.parseDateTime(dateTimeStr);
    }

    public static String getTimeStringFromSecond(Long value) {
        DateTime dateTime = new DateTime(value);
        return dateTime.toString(format);
    }

    public static String getFormattedDuration(long duration) {
        duration = duration < 0 ? 0 : duration;
        return new Duration(duration, TimeUnit.MILLISECONDS).convertToMostSuccinctTimeUnit().toString();
    }

    public static String formatDate(DateTime dateTime) {
        return dateTime.toString(format);
    }

    public static String getCurrentFormatDate() {
        return dateFormat.print(System.currentTimeMillis());
    }

    public static String formatDateTime(DateTime dateTime) {
        return dateTime.toString(dateFormat);
    }

    /**
     * 集群申请/扩容/续费时 用于计算间隔天数
     * @param useType
     * @param userTime
     * @return
     */
//    public static int getIntervalDayForApplyCluster(String useType, int userTime) {
//        DateTime now = DateTime.now();
//        DateTime endDate = getEndDayForApplyCluster(now, useType, userTime);
//        return Days.daysBetween(now, endDate).getDays();
//    }

    /**
     * 集群申请/扩容/续费时 用于计算间隔天数
     * @param useType
     * @param userTime
     * @return
     */
//    public static int getIntervalDayForApplyCluster(DateTime dateTime, String useType, int userTime) {
//        DateTime endDate = getEndDayForApplyCluster(dateTime, useType, userTime);
//        return Days.daysBetween(dateTime, endDate).getDays();
//    }

    /**
     * 集群申请/扩容/续费时 用于计算最后的有效日期
     * @param now
     * @param useType
     * @param userTime
     * @return
     */
//    public static DateTime getEndDayForApplyCluster(DateTime now, String useType, int userTime) {
//        DateTime endDate;
//        switch (useType) {
//            case "日" :
//                endDate = now.plusDays(userTime);
//                break;
//            case "月" :
//                endDate = now.plusMonths(userTime);
//                break;
//            case "季度" :
//                endDate = now.plusMonths(3 * userTime);
//                break;
//            default :
//                endDate = now;
//                break;
//        }
//        return endDate;
//    }
}

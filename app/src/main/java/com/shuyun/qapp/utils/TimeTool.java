package com.shuyun.qapp.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sunxiao on 2018/5/8.
 * 时间工具类
 */
public class TimeTool {

    /**
     * 一些时间格式
     */
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_MILL_TIME = "HH:mm:ss";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_MONTH_DAY_TIME = "MM-dd HH:mm";
    public final static String FORMAT_DATE = "yyyy-MM-dd";

    public static String getFormatToday(String dateFormat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.CHINA);
        return formatter.format(currentTime);
    }

    /**
     * 返回时间格式
     *
     * @param Time
     * @return
     */
    public static String getTimeMill(String Time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return date.toString();
    }


    /**
     * 返回时间格式
     *
     * @param Time
     * @return
     */
    public static String getTime(String Time) {
        Date currentTime = new Date(Long.parseLong(Time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return formatter.format(currentTime);
    }

    /**
     * 返回时间格式
     *
     * @param Time
     * @return
     */
    public static String getTime1(String Time) {
        Date currentTime = new Date(Long.parseLong(Time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        return formatter.format(currentTime);
    }

    /**
     * 返回时间格式
     *
     * @param Time
     * @return
     */
    public static String getCommTime(String Time, String format) {
        Date currentTime = new Date(Long.parseLong(Time));
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return formatter.format(currentTime);
    }

    /**
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.CHINA);
        return formatter.format(date);
    }


}

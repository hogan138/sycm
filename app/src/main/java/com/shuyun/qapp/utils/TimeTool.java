package com.shuyun.qapp.utils;


import android.annotation.SuppressLint;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


import static android.icu.text.DateTimePatternGenerator.DAY;
import static java.util.Calendar.HOUR;


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
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(currentTime);
    }

//
//    public static Date stringToDate(String dateStr, String dateFormat) {
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        try {
//            return formatter.parse(dateStr);
//        } catch (ParseException e) {
//            return null;
//        }
//    }

    /**
     * 返回时间格式
     *
     * @param Time
     * @return
     */
    public static String getTimeMill(String Time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }

    /**
     * 返回时间格式
     *
     * @param Time
     * @return
     */
    public static String getCommTime(String Time,String format) {
        Date currentTime = new Date(Long.parseLong(Time));
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    /**
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }


}

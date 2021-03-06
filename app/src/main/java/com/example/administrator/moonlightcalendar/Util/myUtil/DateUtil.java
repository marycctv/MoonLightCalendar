package com.example.administrator.moonlightcalendar.Util.myUtil;

import com.example.administrator.moonlightcalendar.Util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/6 0006.
 */

public class DateUtil {

    public static final int DAY_MILLIS = 24 * 60 * 60 * 1000;
    public static final String DAY_FORMAT = "yyyy-MM-dd";

    /**
     * 转化为时间string
     * */
    public static String date2String(Date date) {
        return TimeUtils.date2String(date, DAY_FORMAT);
    }

    public static Date string2Date(String dateStr) {
        return TimeUtils.string2Date(dateStr, DAY_FORMAT);
    }

    /**
     * 判断两个日期是否为同一天
     * */
    public static boolean isTheSameDay(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(date2);
        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        int day2 = calendar.get(Calendar.DAY_OF_MONTH);

        return day1 == day2 && month1 == month2 && year1 == year2;
    }

    /**
     * 获取两个日期之间相隔的天数
     * */
    public static int DifferOfDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                //闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 获取当月月份的天数
     * */
    public static int getMonthDaysCount(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Calendar dayAfter(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    public static Calendar monthAfter(Calendar calendar) {
        calendar.add(Calendar.MONTH, 1);
        return calendar;
    }

    public static Calendar weekAfter(Calendar calendar) {
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        return calendar;
    }
}

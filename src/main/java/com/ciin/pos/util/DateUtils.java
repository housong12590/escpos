package com.ciin.pos.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static Date now() {
        return new Date();
    }

    public static String dateToString() {
        return dateToString(now());
    }

    public static DateFormat getDefaultFormat() {
        return DEFAULT_FORMAT;
    }

    public static String dateToString(Date date) {
        return DEFAULT_FORMAT.format(date);
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        return format1.format(date);
    }

    public static Date stringToDate(String date) {
        try {
            return DEFAULT_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Date stringToDate(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static int getAllDaysOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public static int getDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static int getYears(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    public static Long stringDateToStamp(String stringDate) {
        return DateUtils.stringToDate(stringDate).getTime();
    }

    public static String stampToStringDate(Long timeStamp) {
        long l = timeStamp;
        return DateUtils.dateToString(new Date(l));
    }

    public static String businessDate(int resetTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Date date;
        if (hour < resetTime) {
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, day - 1);
        }
        date = calendar.getTime();
        return DATE_FORMAT.format(date) + String.format(" %02d:00:00", resetTime);
    }

    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}

package com.zhiyu.quanzhu.utils;

import java.util.Calendar;

public class CalendarUtils {

    private static CalendarUtils utils;

    public static CalendarUtils getInstance() {
        if (null == utils) {
            synchronized (CalendarUtils.class) {
                utils = new CalendarUtils();
            }
        }
        return utils;
    }

    public int getDaysByYearMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);//注意一定要写5，不要写6！Calendar.MONTH是从0到11的！
        int n = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return n;
    }

    public int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        return year;
    }

    public int getCurrentMonth() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        return month;
    }

    public int getCurrentDay() {
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);
        return day;
    }


}

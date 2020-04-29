package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.CheckIn;

import java.util.ArrayList;

public class CheckInData {
    private int days;
    private int weekdays;
    private String url;
    private boolean is_sgin;
    private ArrayList<CheckIn> list;

    public int getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(int weekdays) {
        this.weekdays = weekdays;
    }

    public boolean isIs_sgin() {
        return is_sgin;
    }

    public void setIs_sgin(boolean is_sgin) {
        this.is_sgin = is_sgin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public ArrayList<CheckIn> getList() {
        return list;
    }

    public void setList(ArrayList<CheckIn> list) {
        this.list = list;
    }
}

package com.zhiyu.quanzhu.model.bean;

import java.util.ArrayList;
import java.util.List;

public class ShopProfile {
    private int shop_id;
    private String shop_name;
    private String shop_icon;
    private int days;
    private float mark;
    private float goods_mark;
    private float service_mark;
    private float kd_mark;
    private int goods_num;
    private int follow_num;
    private int sale_num;
    private boolean is_follow;
    private ArrayList<String> license;
    private int collect_num;
    private boolean is_collect;

    public boolean isIs_collect() {
        return is_collect;
    }

    public void setIs_collect(boolean is_collect) {
        this.is_collect = is_collect;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_icon() {
        return shop_icon;
    }

    public void setShop_icon(String shop_icon) {
        this.shop_icon = shop_icon;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public float getGoods_mark() {
        return goods_mark;
    }

    public void setGoods_mark(float goods_mark) {
        this.goods_mark = goods_mark;
    }

    public float getService_mark() {
        return service_mark;
    }

    public void setService_mark(float service_mark) {
        this.service_mark = service_mark;
    }

    public float getKd_mark() {
        return kd_mark;
    }

    public void setKd_mark(float kd_mark) {
        this.kd_mark = kd_mark;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public boolean isIs_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public ArrayList<String> getLicense() {
        return license;
    }

    public void setLicense(ArrayList<String> license) {
        this.license = license;
    }
}

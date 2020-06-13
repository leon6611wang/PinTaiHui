package com.zhiyu.quanzhu.model.bean;

public class KaQuanTongZhi {
    private int uid;
    private String title;
    private String coupon_title;
    private String coupon_content;
    private String coupon_thumb;
    private String add_time;
    private int shop_id;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoupon_title() {
        return coupon_title;
    }

    public void setCoupon_title(String coupon_title) {
        this.coupon_title = coupon_title;
    }

    public String getCoupon_content() {
        return coupon_content;
    }

    public void setCoupon_content(String coupon_content) {
        this.coupon_content = coupon_content;
    }

    public String getCoupon_thumb() {
        return coupon_thumb;
    }

    public void setCoupon_thumb(String coupon_thumb) {
        this.coupon_thumb = coupon_thumb;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}

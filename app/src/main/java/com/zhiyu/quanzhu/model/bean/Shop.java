package com.zhiyu.quanzhu.model.bean;

public class Shop {
    private long shop_id;
    private String shop_name;
    private String shop_icon;
    private int follow_num;
    private int mark;
    private boolean is_follow;
    private boolean is_collect;
    private long kefu_id;

    public boolean isIs_collect() {
        return is_collect;
    }

    public void setIs_collect(boolean is_collect) {
        this.is_collect = is_collect;
    }

    @Override
    public String toString() {
        return "shop_id: "+shop_id+" , shop_name: "+shop_name+" , shop_icon: "+shop_icon+" , follow_num: "+follow_num+" , mark: "+mark+" , is_follow: "+is_follow+" , kefu_id: "+kefu_id;
    }

    public long getShop_id() {
        return shop_id;
    }

    public void setShop_id(long shop_id) {
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

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public boolean isIs_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public long getKefu_id() {
        return kefu_id;
    }

    public void setKefu_id(long kefu_id) {
        this.kefu_id = kefu_id;
    }
}

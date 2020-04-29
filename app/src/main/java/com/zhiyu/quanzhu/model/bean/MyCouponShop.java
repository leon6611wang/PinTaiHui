package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class MyCouponShop {
    private String name;
    private int shop_id;
    private String icon;
    private List<MyCoupon> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MyCoupon> getList() {
        return list;
    }

    public void setList(List<MyCoupon> list) {
        this.list = list;
    }
}

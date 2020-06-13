package com.zhiyu.quanzhu.model.bean;

import java.io.Serializable;

public class ConversationCircleShop implements Serializable{
    private int id;
    private int quanzi_id;
    private String name;
    private String icon;
    private String logo;
    private String city_name;
    private String shop_type_name;
    private float mark;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuanzi_id() {
        return quanzi_id;
    }

    public void setQuanzi_id(int quanzi_id) {
        this.quanzi_id = quanzi_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getShop_type_name() {
        return shop_type_name;
    }

    public void setShop_type_name(String shop_type_name) {
        this.shop_type_name = shop_type_name;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }
}

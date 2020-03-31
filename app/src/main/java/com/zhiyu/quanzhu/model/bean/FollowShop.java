package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 *
 */
public class FollowShop {
    private int id;
    private String name;
    private String icon;
    private int sale;
    private int score;
    private String city_name;
    private String shop_type_name;
    private List<FollowGoods> goods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public List<FollowGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<FollowGoods> goods) {
        this.goods = goods;
    }
}

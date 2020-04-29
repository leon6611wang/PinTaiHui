package com.zhiyu.quanzhu.model.bean;

/**
 * 积分兑换的商品
 */
public class PointGoods {
    private int id;
    private String goods_name;
    private String thumb;
    private int goods_stock;
    private int goods_price;
    private int goods_credit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(int goods_stock) {
        this.goods_stock = goods_stock;
    }

    public int getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(int goods_price) {
        this.goods_price = goods_price;
    }

    public int getGoods_credit() {
        return goods_credit;
    }

    public void setGoods_credit(int goods_credit) {
        this.goods_credit = goods_credit;
    }
}

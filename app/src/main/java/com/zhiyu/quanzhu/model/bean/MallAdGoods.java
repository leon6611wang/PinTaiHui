package com.zhiyu.quanzhu.model.bean;

/**
 * 商城首页广告商品
 */
public class MallAdGoods {
    private long id;
    private String img;
    private String goods_name;
    private int goods_stock;
    private long goods_price;
    private int sale_num;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(int goods_stock) {
        this.goods_stock = goods_stock;
    }

    public long getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(long goods_price) {
        this.goods_price = goods_price;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }
}

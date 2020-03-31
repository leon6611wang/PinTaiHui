package com.zhiyu.quanzhu.model.bean;

/**
 * 我的关注-商店-商品
 */
public class FollowGoods {
    private int id;
    private String goods_name;
    private int goods_price;
    private int goods_stock;
    private int sale_num;
    private FollowGoodsImg img;

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

    public int getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(int goods_price) {
        this.goods_price = goods_price;
    }

    public int getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(int goods_stock) {
        this.goods_stock = goods_stock;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public FollowGoodsImg getImg() {
        return img;
    }

    public void setImg(FollowGoodsImg img) {
        this.img = img;
    }
}

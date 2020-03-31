package com.zhiyu.quanzhu.model.bean;

public class CircleInfoShopGoods {
    private int id;
    private int shop_id;
    private String goods_name;
    private int goods_price;
    private int sale_num;
    private CircleInfoShopGoodsImg img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
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

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public CircleInfoShopGoodsImg getImg() {
        return img;
    }

    public void setImg(CircleInfoShopGoodsImg img) {
        this.img = img;
    }
}

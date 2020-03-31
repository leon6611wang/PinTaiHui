package com.zhiyu.quanzhu.model.bean;


public class FullSearchGoods {
    private int id;
    private String goods_name;
    private int goods_price;
    private int goods_stock;
    private int sale_num;
    private int shop_id;
    private FullSearchGoodsImage img;
    private boolean is_relation;

    public boolean isIs_relation() {
        return is_relation;
    }

    public void setIs_relation(boolean is_relation) {
        this.is_relation = is_relation;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

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

    public FullSearchGoodsImage getImg() {
        return img;
    }

    public void setImg(FullSearchGoodsImage img) {
        this.img = img;
    }
}

package com.zhiyu.quanzhu.model.bean;

public class OrderConfirmGoods {
    private int goods_id;
    private int num;
    private String nid;
    private String norms_id;
    private int goods_feight;
    private String goods_name;
    private int goods_status;
    private String norms_name;
    private int stock;
    private int price;
    private int market_price;
    private String img;

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNorms_id() {
        return norms_id;
    }

    public void setNorms_id(String norms_id) {
        this.norms_id = norms_id;
    }

    public int getMarket_price() {
        return market_price;
    }

    public void setMarket_price(int market_price) {
        this.market_price = market_price;
    }

    public int getGoods_feight() {
        return goods_feight;
    }

    public void setGoods_feight(int goods_feight) {
        this.goods_feight = goods_feight;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(int goods_status) {
        this.goods_status = goods_status;
    }

    public String getNorms_name() {
        return norms_name;
    }

    public void setNorms_name(String norms_name) {
        this.norms_name = norms_name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

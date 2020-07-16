package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class PointGoodsInformation {
    private int id;
    private String goods_name;
    private int goods_stock;
    private int goods_price;
    private int credits;
    private String desc;
    private List<GoodsImg> pics;
    private List<GoodsImg> img_json;


    public List<GoodsImg> getImg_json() {
        return img_json;
    }

    public void setImg_json(List<GoodsImg> img_json) {
        this.img_json = img_json;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<GoodsImg> getPics() {
        return pics;
    }

    public void setPics(List<GoodsImg> pics) {
        this.pics = pics;
    }
}

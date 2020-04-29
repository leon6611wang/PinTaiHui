package com.zhiyu.quanzhu.model.bean;

public class FooterPrintHistoryGoods {
    private int id;
    private String goods_name;
    private int goods_price;
    private int sale_num;
    private GoodsImg img;

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

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public GoodsImg getImg() {
        return img;
    }

    public void setImg(GoodsImg img) {
        this.img = img;
    }
}

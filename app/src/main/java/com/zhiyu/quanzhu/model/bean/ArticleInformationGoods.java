package com.zhiyu.quanzhu.model.bean;

public class ArticleInformationGoods {
    private int goods_id;
    private String goods_name;
    private int goods_price;
    private int sale_num;
    private ArticleInformationGoodsImg img;

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
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

    public ArticleInformationGoodsImg getImg() {
        return img;
    }

    public void setImg(ArticleInformationGoodsImg img) {
        this.img = img;
    }
}

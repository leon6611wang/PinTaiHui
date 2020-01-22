package com.zhiyu.quanzhu.model.bean;

/**
 * 购物车商品
 */
public class CartGoods {
    private long id;
    private int num;
    private long nid;
    private int goods_feight;
    private long goods_id;
    private String goods_name;
    private int goods_status;
    private String norms_name;
    private int stock;
    private long price;
    private String img;
    private int currentNum;//当前选择的商品数量
    private boolean isSelected;//当前商品是否选中

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            if (stock == 0 || currentNum > stock) {
                this.isSelected = false;
            } else {
                this.isSelected = selected;
            }
        } else {
            isSelected = selected;
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getNid() {
        return nid;
    }

    public void setNid(long nid) {
        this.nid = nid;
    }

    public int getGoods_feight() {
        return goods_feight;
    }

    public void setGoods_feight(int goods_feight) {
        this.goods_feight = goods_feight;
    }

    public long getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(long goods_id) {
        this.goods_id = goods_id;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

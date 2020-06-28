package com.zhiyu.quanzhu.model.bean;

public class AfterSaleOrder {
    private int id;
    private int shop_id;
    private String shop_name;
    private String shop_logo;
    private int goods_id;
    private String goods_name;
    private long goods_price;
    private String goods_img;
    private int goods_num;
    private  String goods_normas_name;
    private long refund_price;
    private long total_price;
    private int refund_type;
    private int refund_status;
    private boolean is_kefu;
    private int kefu_status;

    public int getKefu_status() {
        return kefu_status;
    }

    public void setKefu_status(int kefu_status) {
        this.kefu_status = kefu_status;
    }

    public long getTotal_price() {
        return total_price;
    }

    public void setTotal_price(long total_price) {
        this.total_price = total_price;
    }

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

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

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

    public long getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(long goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_normas_name() {
        return goods_normas_name;
    }

    public void setGoods_normas_name(String goods_normas_name) {
        this.goods_normas_name = goods_normas_name;
    }

    public long getRefund_price() {
        return refund_price;
    }

    public void setRefund_price(long refund_price) {
        this.refund_price = refund_price;
    }

    public int getRefund_type() {
        return refund_type;
    }

    public void setRefund_type(int refund_type) {
        this.refund_type = refund_type;
    }

    public int getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(int refund_status) {
        this.refund_status = refund_status;
    }

    public boolean isIs_kefu() {
        return is_kefu;
    }

    public void setIs_kefu(boolean is_kefu) {
        this.is_kefu = is_kefu;
    }
}

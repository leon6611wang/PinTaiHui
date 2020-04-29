package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class OrderInformationGoods {
    private int id;
    private int goods_id;
    private String goods_name;
    private String norms_name;
    private String goods_img;
    private long goods_price;
    private int goods_num;
    private int refund;//1.仅退款  2.退货退款,3换货
    private String refund_desc;
    private long refund_price;
    private int refund_id;
    private String refund_remark;
    private List<UploadImage> refund_img_list;

    public String getRefund_remark() {
        return refund_remark;
    }

    public void setRefund_remark(String refund_remark) {
        this.refund_remark = refund_remark;
    }

    public List<UploadImage> getRefund_img_list() {
        return refund_img_list;
    }

    public void setRefund_img_list(List<UploadImage> refund_img_list) {
        this.refund_img_list = refund_img_list;
    }

    public int getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(int refund_id) {
        this.refund_id = refund_id;
    }

    public long getRefund_price() {
        return refund_price;
    }

    public void setRefund_price(long refund_price) {
        this.refund_price = refund_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNorms_name() {
        return norms_name;
    }

    public void setNorms_name(String norms_name) {
        this.norms_name = norms_name;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public long getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(long goods_price) {
        this.goods_price = goods_price;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public String getRefund_desc() {
        return refund_desc;
    }

    public void setRefund_desc(String refund_desc) {
        this.refund_desc = refund_desc;
    }
}

package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class AfterSaleServiceInformation {
    private int id;
    private int goods_id;
    private String goods_name;
    private String goods_img;
    private int goods_num;
    private long goods_price;
    private int refund;
    private String reason;
    private String addtime;
    private String refund_sn;
    private String remark;
    private List<UploadImage> img;
    private String wl_no;
    private String wl_company;
    private int status;
    private String title;
    private String desc;
    private long time;
    private long refund_price;
    private String norms_name;

    public long getRefund_price() {
        return refund_price;
    }

    public void setRefund_price(long refund_price) {
        this.refund_price = refund_price;
    }

    public String getNorms_name() {
        return norms_name;
    }

    public void setNorms_name(String norms_name) {
        this.norms_name = norms_name;
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

    public long getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(long goods_price) {
        this.goods_price = goods_price;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getRefund_sn() {
        return refund_sn;
    }

    public void setRefund_sn(String refund_sn) {
        this.refund_sn = refund_sn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<UploadImage> getImg() {
        return img;
    }

    public void setImg(List<UploadImage> img) {
        this.img = img;
    }

    public String getWl_no() {
        return wl_no;
    }

    public void setWl_no(String wl_no) {
        this.wl_no = wl_no;
    }

    public String getWl_company() {
        return wl_company;
    }

    public void setWl_company(String wl_company) {
        this.wl_company = wl_company;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

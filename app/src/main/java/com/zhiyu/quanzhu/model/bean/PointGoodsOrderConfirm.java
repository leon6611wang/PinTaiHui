package com.zhiyu.quanzhu.model.bean;

/**
 * 积分商品-订单确认
 */
public class PointGoodsOrderConfirm {
    private String goods_name;
    private int num;
    private int need_inegral;
    private Address address;
    private String thumb;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNeed_inegral() {
        return need_inegral;
    }

    public void setNeed_inegral(int need_inegral) {
        this.need_inegral = need_inegral;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

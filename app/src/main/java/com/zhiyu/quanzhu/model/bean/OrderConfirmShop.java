package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class OrderConfirmShop {
    private int shop_id;
    private String name;
    private String icon;
    private int status;
    private int business_status;
    private int goods_num;
    private int all_price;
    private int freight;
    private List<OrderConfirmGoods> list;
    private String remark;
    private int postage_price;//邮费
    private int discount_price;//优惠价格
    private int pay_price;
    private boolean useCoupon;

    public boolean isUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(boolean useCoupon) {
        this.useCoupon = useCoupon;
    }

    public int getPostage_price() {
        return postage_price;
    }

    public void setPostage_price(int postage_price) {
        this.postage_price = postage_price;
    }

    public int getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(int discount_price) {
        this.discount_price = discount_price;
    }

    public int getPay_price() {
        return pay_price;
    }

    public void setPay_price(int pay_price) {
        this.pay_price = pay_price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderConfirmGoods> getList() {
        return list;
    }

    public void setList(List<OrderConfirmGoods> list) {
        this.list = list;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(int business_status) {
        this.business_status = business_status;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public int getAll_price() {
        return all_price;
    }

    public void setAll_price(int all_price) {
        this.all_price = all_price;
    }

    public int getFreight() {
        return freight;
    }

    public void setFreight(int freight) {
        this.freight = freight;
    }
}

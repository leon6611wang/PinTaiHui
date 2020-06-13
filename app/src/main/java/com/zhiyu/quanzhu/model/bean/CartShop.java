package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 购物车商店
 */
public class CartShop {
    private String name;
    private String shop_id;
    private String icon;
    private int status;
    private int business_status;
    private int goods_num;
    private int all_price;
    private int freight;
    private List<CartGoods> list;
    private boolean isSelected;
    private boolean has_counpon;

    public boolean isHas_counpon() {
        return has_counpon;
    }

    public void setHas_counpon(boolean has_counpon) {
        this.has_counpon = has_counpon;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<CartGoods> getList() {
        return list;
    }

    public void setList(List<CartGoods> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

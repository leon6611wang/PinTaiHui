package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class CircleInfoShop {
    private int id;
    private String icon;
    private int mark;
    private String name;
    private String city_name;
    private String shop_type_name;
    private int kefu_num;
    private int goods_num;
    private int collect_num;
    private int view_num;
    private int sale_num;
    private int sale_amount;
    private int business_status;
    private String created_at;
    private List<CircleInfoShopGoods> goods_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getShop_type_name() {
        return shop_type_name;
    }

    public void setShop_type_name(String shop_type_name) {
        this.shop_type_name = shop_type_name;
    }

    public int getKefu_num() {
        return kefu_num;
    }

    public void setKefu_num(int kefu_num) {
        this.kefu_num = kefu_num;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public int getView_num() {
        return view_num;
    }

    public void setView_num(int view_num) {
        this.view_num = view_num;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public int getSale_amount() {
        return sale_amount;
    }

    public void setSale_amount(int sale_amount) {
        this.sale_amount = sale_amount;
    }

    public int getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(int business_status) {
        this.business_status = business_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<CircleInfoShopGoods> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<CircleInfoShopGoods> goods_list) {
        this.goods_list = goods_list;
    }
}

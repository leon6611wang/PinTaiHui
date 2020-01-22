package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 购物车商店
 */
public class CartShop {
    private String name;
    private String shop_id;
    private String icon;
    private List<CartGoods> list;
    private boolean isSelected;


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

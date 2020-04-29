package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.FullSearchShop;

import java.util.List;

public class FullSearchShopData {
    private List<FullSearchShop> shop_list;
    private List<FullSearchShop> list;

    public List<FullSearchShop> getList() {
        return list;
    }

    public void setList(List<FullSearchShop> list) {
        this.list = list;
    }

    public List<FullSearchShop> getShop_list() {
        return shop_list;
    }

    public void setShop_list(List<FullSearchShop> shop_list) {
        this.shop_list = shop_list;
    }
}

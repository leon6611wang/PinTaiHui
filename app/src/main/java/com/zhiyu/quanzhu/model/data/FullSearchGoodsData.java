package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.FullSearchGoods;

import java.util.List;

public class FullSearchGoodsData {
    private List<FullSearchGoods> goods_list;
    private List<FullSearchGoods> goods;

    public List<FullSearchGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<FullSearchGoods> goods) {
        this.goods = goods;
    }

    public List<FullSearchGoods> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<FullSearchGoods> goods_list) {
        this.goods_list = goods_list;
    }
}

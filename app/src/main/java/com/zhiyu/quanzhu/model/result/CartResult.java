package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CartShop;

import java.util.List;

public class CartResult extends BaseResult {
    private List<CartShop> data;

    public List<CartShop> getData() {
        return data;
    }

    public void setData(List<CartShop> data) {
        this.data = data;
    }
}

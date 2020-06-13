package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.OrderAdd;

public class OrderAddResult extends BaseResult {
    private OrderAdd data;

    public OrderAdd getData() {
        return data;
    }

    public void setData(OrderAdd data) {
        this.data = data;
    }
}

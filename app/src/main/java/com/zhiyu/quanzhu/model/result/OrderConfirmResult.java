package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.OrderConfirmData;

public class OrderConfirmResult extends BaseResult {
    private OrderConfirmData data;

    public OrderConfirmData getData() {
        return data;
    }

    public void setData(OrderConfirmData data) {
        this.data = data;
    }
}

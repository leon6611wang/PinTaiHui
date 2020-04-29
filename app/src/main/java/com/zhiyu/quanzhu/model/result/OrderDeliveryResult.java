package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.OrderDeliveryData;

public class OrderDeliveryResult extends BaseResult {
    private OrderDeliveryData data;

    public OrderDeliveryData getData() {
        return data;
    }

    public void setData(OrderDeliveryData data) {
        this.data = data;
    }
}

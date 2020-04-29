package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.OrderInformationData;

public class OrderInformationResult extends BaseResult {
    private OrderInformationData data;

    public OrderInformationData getData() {
        return data;
    }

    public void setData(OrderInformationData data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.AfterSaleOrderData;

public class AfterSaleOrderResult extends BaseResult {
    private AfterSaleOrderData data;

    public AfterSaleOrderData getData() {
        return data;
    }

    public void setData(AfterSaleOrderData data) {
        this.data = data;
    }
}

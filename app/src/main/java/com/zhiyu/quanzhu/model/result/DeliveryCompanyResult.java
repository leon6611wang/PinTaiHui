package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.DeliveryCompanyData;

public class DeliveryCompanyResult extends BaseResult {
    private DeliveryCompanyData data;

    public DeliveryCompanyData getData() {
        return data;
    }

    public void setData(DeliveryCompanyData data) {
        this.data = data;
    }
}

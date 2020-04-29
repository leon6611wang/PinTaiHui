package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.DeliveryInfoData;

public class DeliveryInfoResult extends BaseResult {
    private DeliveryInfoData data;

    public DeliveryInfoData getData() {
        return data;
    }

    public void setData(DeliveryInfoData data) {
        this.data = data;
    }
}

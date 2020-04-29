package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.AfterSaleServiceInformationData;

public class AfterSaleServiceInformationResult extends BaseResult {
    private AfterSaleServiceInformationData data;

    public AfterSaleServiceInformationData getData() {
        return data;
    }

    public void setData(AfterSaleServiceInformationData data) {
        this.data = data;
    }
}

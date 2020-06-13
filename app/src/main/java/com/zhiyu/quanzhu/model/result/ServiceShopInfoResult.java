package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ServiceShopInfoData;

public class ServiceShopInfoResult extends BaseResult {
    private ServiceShopInfoData data;

    public ServiceShopInfoData getData() {
        return data;
    }

    public void setData(ServiceShopInfoData data) {
        this.data = data;
    }
}

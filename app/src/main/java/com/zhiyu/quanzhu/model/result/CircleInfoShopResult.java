package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CircleInfoShopData;

public class CircleInfoShopResult extends BaseResult {
    private CircleInfoShopData data;

    public CircleInfoShopData getData() {
        return data;
    }

    public void setData(CircleInfoShopData data) {
        this.data = data;
    }
}

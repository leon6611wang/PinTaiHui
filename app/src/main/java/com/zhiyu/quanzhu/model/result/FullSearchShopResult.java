package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FullSearchShopData;

public class FullSearchShopResult extends BaseResult {
    private FullSearchShopData data;

    public FullSearchShopData getData() {
        return data;
    }

    public void setData(FullSearchShopData data) {
        this.data = data;
    }
}

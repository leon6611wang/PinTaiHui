package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.StoreData;


public class StoreResult extends BaseResult {
    private StoreData data;

    public StoreData getData() {
        return data;
    }

    public void setData(StoreData data) {
        this.data = data;
    }
}

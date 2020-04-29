package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.VIPData;

public class VIPResult extends BaseResult {
    private VIPData data;

    public VIPData getData() {
        return data;
    }

    public void setData(VIPData data) {
        this.data = data;
    }
}

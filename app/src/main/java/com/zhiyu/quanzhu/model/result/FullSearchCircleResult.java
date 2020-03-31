package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FullSearchCircleData;

public class FullSearchCircleResult extends BaseResult {
    private FullSearchCircleData data;

    public FullSearchCircleData getData() {
        return data;
    }

    public void setData(FullSearchCircleData data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.InterestCircleData;

public class InterestCircleResult extends BaseResult {
    private InterestCircleData data;

    public InterestCircleData getData() {
        return data;
    }

    public void setData(InterestCircleData data) {
        this.data = data;
    }
}

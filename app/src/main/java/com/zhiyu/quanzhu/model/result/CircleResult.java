package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CircleData;

public class CircleResult extends BaseResult {
    private CircleData data;

    public CircleData getData() {
        return data;
    }

    public void setData(CircleData data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.MyCircleData;

public class MyCircleResult extends BaseResult {
    private MyCircleData data;

    public MyCircleData getData() {
        return data;
    }

    public void setData(MyCircleData data) {
        this.data = data;
    }
}

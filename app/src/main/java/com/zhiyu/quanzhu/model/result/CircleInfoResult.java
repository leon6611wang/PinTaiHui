package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CircleInfo;

public class CircleInfoResult extends BaseResult {
    private CircleInfo data;

    public CircleInfo getData() {
        return data;
    }

    public void setData(CircleInfo data) {
        this.data = data;
    }
}

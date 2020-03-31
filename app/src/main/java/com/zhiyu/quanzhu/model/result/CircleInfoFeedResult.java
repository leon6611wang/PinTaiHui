package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CircleInfoFeedData;

public class CircleInfoFeedResult extends BaseResult {
    private CircleInfoFeedData data;

    public CircleInfoFeedData getData() {
        return data;
    }

    public void setData(CircleInfoFeedData data) {
        this.data = data;
    }
}

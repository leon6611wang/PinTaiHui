package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FeedData;

public class FeedResult extends BaseResult {
    private FeedData data;

    public FeedData getData() {
        return data;
    }

    public void setData(FeedData data) {
        this.data = data;
    }
}

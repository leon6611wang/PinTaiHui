package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FullSearchFeedData;

public class FullSearchFeedResult extends BaseResult {
    private FullSearchFeedData data;

    public FullSearchFeedData getData() {
        return data;
    }

    public void setData(FullSearchFeedData data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FeedInfoData;

public class FeedInfoResult extends BaseResult {
    private FeedInfoData data;

    public FeedInfoData getData() {
        return data;
    }

    public void setData(FeedInfoData data) {
        this.data = data;
    }
}

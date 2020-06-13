package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FeedInfoData;
import com.zhiyu.quanzhu.model.data.FeedInformationData;

public class FeedInformationResult extends BaseResult {
    private FeedInformationData data;

    public FeedInformationData getData() {
        return data;
    }

    public void setData(FeedInformationData data) {
        this.data = data;
    }
}

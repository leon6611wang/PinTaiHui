package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.AddFeedData;

public class AddFeedResult extends BaseResult {
    private AddFeedData data;

    public AddFeedData getData() {
        return data;
    }

    public void setData(AddFeedData data) {
        this.data = data;
    }
}

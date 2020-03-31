package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FeedCommentData;

public class FeedCommentResult extends BaseResult {
    private FeedCommentData data;

    public FeedCommentData getData() {
        return data;
    }

    public void setData(FeedCommentData data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FeedCommentChildData;

public class FeedChildCommentResult extends BaseResult {
    private FeedCommentChildData data;

    public FeedCommentChildData getData() {
        return data;
    }

    public void setData(FeedCommentChildData data) {
        this.data = data;
    }
}

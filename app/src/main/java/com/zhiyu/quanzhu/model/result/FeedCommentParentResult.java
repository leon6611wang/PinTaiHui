package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FeedCommentParent;

public class FeedCommentParentResult extends BaseResult {

    private FeedCommentParent data;

    public FeedCommentParent getData() {
        return data;
    }

    public void setData(FeedCommentParent data) {
        this.data = data;
    }
}

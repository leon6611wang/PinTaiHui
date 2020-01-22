package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CommentData;

public class CommentResult extends BaseResult{
    private CommentData data;
    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }
}

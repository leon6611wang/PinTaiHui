package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CommentChildData;

public class CommentChildResult extends BaseResult {
    private CommentChildData data;

    public CommentChildData getData() {
        return data;
    }

    public void setData(CommentChildData data) {
        this.data = data;
    }
}

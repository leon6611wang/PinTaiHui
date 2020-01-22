package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.GoodsCommentData;

public class GoodsCommentResult extends BaseResult {
    private GoodsCommentData data;

    public GoodsCommentData getData() {
        return data;
    }

    public void setData(GoodsCommentData data) {
        this.data = data;
    }
}

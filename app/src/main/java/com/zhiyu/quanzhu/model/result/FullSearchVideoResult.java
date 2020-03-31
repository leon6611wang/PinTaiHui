package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FullSearchVideoData;

public class FullSearchVideoResult extends BaseResult {
    private FullSearchVideoData data;

    public FullSearchVideoData getData() {
        return data;
    }

    public void setData(FullSearchVideoData data) {
        this.data = data;
    }
}

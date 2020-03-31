package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FullSearchArticleData;

public class FullSearchArticleResult extends BaseResult {
    private FullSearchArticleData data;

    public FullSearchArticleData getData() {
        return data;
    }

    public void setData(FullSearchArticleData data) {
        this.data = data;
    }
}

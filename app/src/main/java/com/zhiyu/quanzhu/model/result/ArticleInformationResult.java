package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ArticleInformationData;

public class ArticleInformationResult extends BaseResult {
    private ArticleInformationData data;

    public ArticleInformationData getData() {
        return data;
    }

    public void setData(ArticleInformationData data) {
        this.data = data;
    }
}

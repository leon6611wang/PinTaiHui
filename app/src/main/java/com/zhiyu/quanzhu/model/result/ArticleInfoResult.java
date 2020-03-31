package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ArticleInfoData;

public class ArticleInfoResult extends BaseResult {

    private ArticleInfoData data;

    public ArticleInfoData getData() {
        return data;
    }

    public void setData(ArticleInfoData data) {
        this.data = data;
    }
}

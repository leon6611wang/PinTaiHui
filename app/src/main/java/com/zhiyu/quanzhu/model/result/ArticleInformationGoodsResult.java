package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ArticleInformationGoodsData;

public class ArticleInformationGoodsResult extends BaseResult {
    private ArticleInformationGoodsData data;

    public ArticleInformationGoodsData getData() {
        return data;
    }

    public void setData(ArticleInformationGoodsData data) {
        this.data = data;
    }
}

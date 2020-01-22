package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.MallAdGoodsData;

public class MallAdGoodsResult extends BaseResult {
    private MallAdGoodsData data;

    public MallAdGoodsData getData() {
        return data;
    }

    public void setData(MallAdGoodsData data) {
        this.data = data;
    }
}

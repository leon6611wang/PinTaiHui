package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FeedsGoodsData;

public class FeedsGoodsResult extends BaseResult {
    private FeedsGoodsData data;

    public FeedsGoodsData getData() {
        return data;
    }

    public void setData(FeedsGoodsData data) {
        this.data = data;
    }
}

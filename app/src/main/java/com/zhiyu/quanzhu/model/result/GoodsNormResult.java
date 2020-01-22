package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.GoodsNormData;

public class GoodsNormResult extends BaseResult {
    private GoodsNormData data;

    public GoodsNormData getData() {
        return data;
    }

    public void setData(GoodsNormData data) {
        this.data = data;
    }
}

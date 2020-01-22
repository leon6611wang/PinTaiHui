package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.GoodsData;

public class GoodsResult extends BaseResult {
    private GoodsData data;

    public GoodsData getData() {
        return data;
    }

    public void setData(GoodsData data) {
        this.data = data;
    }
}

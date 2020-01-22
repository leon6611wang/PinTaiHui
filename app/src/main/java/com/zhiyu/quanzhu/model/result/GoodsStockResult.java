package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.GoodsStockData;

public class GoodsStockResult extends BaseResult {
    private GoodsStockData data;

    public GoodsStockData getData() {
        return data;
    }

    public void setData(GoodsStockData data) {
        this.data = data;
    }
}

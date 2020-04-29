package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.PointGoodsData;

public class PointGoodsResult extends BaseResult {
    private PointGoodsData data;

    public PointGoodsData getData() {
        return data;
    }

    public void setData(PointGoodsData data) {
        this.data = data;
    }
}

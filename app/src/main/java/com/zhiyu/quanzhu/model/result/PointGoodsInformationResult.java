package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.PointGoodsInformationData;

public class PointGoodsInformationResult extends BaseResult {
    private PointGoodsInformationData data;

    public PointGoodsInformationData getData() {
        return data;
    }

    public void setData(PointGoodsInformationData data) {
        this.data = data;
    }
}

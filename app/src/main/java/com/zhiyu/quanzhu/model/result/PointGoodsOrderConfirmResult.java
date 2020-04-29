package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.PointGoodsOrderConfirm;

public class PointGoodsOrderConfirmResult extends BaseResult {
    private PointGoodsOrderConfirm data;

    public PointGoodsOrderConfirm getData() {
        return data;
    }

    public void setData(PointGoodsOrderConfirm data) {
        this.data = data;
    }
}

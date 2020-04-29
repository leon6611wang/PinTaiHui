package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ShopInfoGoodsTypeData;

public class ShopInfoGoodsTypeResult extends BaseResult {
    private ShopInfoGoodsTypeData data;

    public ShopInfoGoodsTypeData getData() {
        return data;
    }

    public void setData(ShopInfoGoodsTypeData data) {
        this.data = data;
    }
}

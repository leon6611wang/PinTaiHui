package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FullSearchGoodsData;

public class FullSearchGoodsResult extends BaseResult {
    private FullSearchGoodsData data;

    public FullSearchGoodsData getData() {
        return data;
    }

    public void setData(FullSearchGoodsData data) {
        this.data = data;
    }
}

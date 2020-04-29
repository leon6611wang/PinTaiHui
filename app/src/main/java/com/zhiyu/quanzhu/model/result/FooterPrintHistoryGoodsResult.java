package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FooterPrintHistoryGoodsData;

public class FooterPrintHistoryGoodsResult extends BaseResult {
    private FooterPrintHistoryGoodsData data;

    public FooterPrintHistoryGoodsData getData() {
        return data;
    }

    public void setData(FooterPrintHistoryGoodsData data) {
        this.data = data;
    }
}

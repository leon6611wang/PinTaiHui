package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.AllVIPEquityData;

public class AllVIPEquityResult extends BaseResult {
    private AllVIPEquityData data;

    public AllVIPEquityData getData() {
        return data;
    }

    public void setData(AllVIPEquityData data) {
        this.data = data;
    }
}

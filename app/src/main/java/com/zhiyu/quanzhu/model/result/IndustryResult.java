package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.IndustryData;

public class IndustryResult extends BaseResult {

    private IndustryData data;

    public IndustryData getData() {
        return data;
    }

    public void setData(IndustryData data) {
        this.data = data;
    }
}

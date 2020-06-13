package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.IndustryHobbyData;

public class IndustryHobbyResult extends BaseResult {
    private IndustryHobbyData data;

    public IndustryHobbyData getData() {
        return data;
    }

    public void setData(IndustryHobbyData data) {
        this.data = data;
    }
}

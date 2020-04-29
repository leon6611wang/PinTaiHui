package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CheckInData;

public class CheckInResult extends BaseResult {
    private CheckInData data;

    public CheckInData getData() {
        return data;
    }

    public void setData(CheckInData data) {
        this.data = data;
    }
}

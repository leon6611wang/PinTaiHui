package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.HomeBaseData;

public class HomeBaseResult extends BaseResult {
    private HomeBaseData data;

    public HomeBaseData getData() {
        return data;
    }

    public void setData(HomeBaseData data) {
        this.data = data;
    }
}

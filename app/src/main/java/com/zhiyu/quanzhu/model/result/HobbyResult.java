package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.HobbyData;

public class HobbyResult extends BaseResult {

    private HobbyData data;

    public HobbyData getData() {
        return data;
    }

    public void setData(HobbyData data) {
        this.data = data;
    }
}

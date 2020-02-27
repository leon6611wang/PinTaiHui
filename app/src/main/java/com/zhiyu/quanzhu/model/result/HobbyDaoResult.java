package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.HobbyDaoData;

public class HobbyDaoResult extends BaseResult {
    private HobbyDaoData data;

    public HobbyDaoData getData() {
        return data;
    }

    public void setData(HobbyDaoData data) {
        this.data = data;
    }
}

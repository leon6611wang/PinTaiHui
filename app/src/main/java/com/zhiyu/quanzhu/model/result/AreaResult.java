package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.AreaData;

public class AreaResult extends BaseResult {
    private AreaData data;

    public AreaData getData() {
        return data;
    }

    public void setData(AreaData data) {
        this.data = data;
    }
}

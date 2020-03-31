package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FansData;

public class FansResult extends BaseResult {
    private FansData data;

    public FansData getData() {
        return data;
    }

    public void setData(FansData data) {
        this.data = data;
    }
}

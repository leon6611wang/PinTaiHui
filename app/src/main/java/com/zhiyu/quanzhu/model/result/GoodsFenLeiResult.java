package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.GoodsFenLeiData;

public class GoodsFenLeiResult extends BaseResult {
    private GoodsFenLeiData data;

    public GoodsFenLeiData getData() {
        return data;
    }

    public void setData(GoodsFenLeiData data) {
        this.data = data;
    }
}

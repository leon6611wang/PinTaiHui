package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FullSearchAll;

public class FullSearchAllResult extends BaseResult {
    private FullSearchAll data;

    public FullSearchAll getData() {
        return data;
    }

    public void setData(FullSearchAll data) {
        this.data = data;
    }
}

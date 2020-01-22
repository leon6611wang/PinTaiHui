package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.MallAdData;

public class MallAdResult extends BaseResult {
    private MallAdData data;

    public MallAdData getData() {
        return data;
    }

    public void setData(MallAdData data) {
        this.data = data;
    }
}

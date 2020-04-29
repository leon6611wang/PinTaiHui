package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.PraiseCircleData;

public class PraiseCircleResult extends BaseResult {
    private PraiseCircleData data;

    public PraiseCircleData getData() {
        return data;
    }

    public void setData(PraiseCircleData data) {
        this.data = data;
    }
}

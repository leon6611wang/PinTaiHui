package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Vertify;

public class VertifyResult extends BaseResult {
    private Vertify data;

    public Vertify getData() {
        return data;
    }

    public void setData(Vertify data) {
        this.data = data;
    }
}

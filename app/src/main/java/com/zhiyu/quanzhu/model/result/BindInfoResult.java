package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.BindInfo;

public class BindInfoResult extends BaseResult {
    private BindInfo data;

    public BindInfo getData() {
        return data;
    }

    public void setData(BindInfo data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.BindMobile;

public class BindMobileResult extends BaseResult {
    private BindMobile data;

    public BindMobile getData() {
        return data;
    }

    public void setData(BindMobile data) {
        this.data = data;
    }
}

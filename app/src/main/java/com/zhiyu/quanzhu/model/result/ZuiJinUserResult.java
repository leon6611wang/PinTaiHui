package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ZuiJinUserData;

public class ZuiJinUserResult extends BaseResult {
    private ZuiJinUserData data;

    public ZuiJinUserData getData() {
        return data;
    }

    public void setData(ZuiJinUserData data) {
        this.data = data;
    }
}

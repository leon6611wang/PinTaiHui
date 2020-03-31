package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CircleInfoUserData;

public class CircleInfoUserResult extends BaseResult {
    private CircleInfoUserData data;

    public CircleInfoUserData getData() {
        return data;
    }

    public void setData(CircleInfoUserData data) {
        this.data = data;
    }
}

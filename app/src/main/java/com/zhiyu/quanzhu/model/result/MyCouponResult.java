package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.MyCouponData;

public class MyCouponResult extends BaseResult {
    private MyCouponData data;

    public MyCouponData getData() {
        return data;
    }

    public void setData(MyCouponData data) {
        this.data = data;
    }
}

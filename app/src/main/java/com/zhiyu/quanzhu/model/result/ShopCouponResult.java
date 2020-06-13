package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ShopCouponData;

public class ShopCouponResult extends BaseResult {
    private ShopCouponData data;

    public ShopCouponData getData() {
        return data;
    }

    public void setData(ShopCouponData data) {
        this.data = data;
    }
}

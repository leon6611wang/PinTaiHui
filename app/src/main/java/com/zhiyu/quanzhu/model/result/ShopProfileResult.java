package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.ShopProfile;

public class ShopProfileResult extends BaseResult {
    private ShopProfile data;

    public ShopProfile getData() {
        return data;
    }

    public void setData(ShopProfile data) {
        this.data = data;
    }
}

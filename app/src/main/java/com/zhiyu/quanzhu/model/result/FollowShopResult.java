package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FollowShopData;

public class FollowShopResult extends BaseResult {
    private FollowShopData data;

    public FollowShopData getData() {
        return data;
    }

    public void setData(FollowShopData data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Shop;

public class ShopResult extends BaseResult {
    private Shop data;

    public Shop getData() {
        return data;
    }

    public void setData(Shop data) {
        this.data = data;
    }
}

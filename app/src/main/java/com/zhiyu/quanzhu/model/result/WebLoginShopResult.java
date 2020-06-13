package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.WebLoginShopData;

public class WebLoginShopResult extends BaseResult {
    private WebLoginShopData data;

    public WebLoginShopData getData() {
        return data;
    }

    public void setData(WebLoginShopData data) {
        this.data = data;
    }
}

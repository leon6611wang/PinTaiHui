package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.LinkShopData;

public class LinkShopResult extends BaseResult {
    private LinkShopData data;

    public LinkShopData getData() {
        return data;
    }

    public void setData(LinkShopData data) {
        this.data = data;
    }
}

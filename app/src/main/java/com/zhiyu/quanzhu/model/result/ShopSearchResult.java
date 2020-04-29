package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ShopSearchData;

public class ShopSearchResult extends BaseResult {
    private ShopSearchData data;

    public ShopSearchData getData() {
        return data;
    }

    public void setData(ShopSearchData data) {
        this.data = data;
    }
}

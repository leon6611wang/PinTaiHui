package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.ShopType;

import java.util.List;

public class ShopTypeResult extends BaseResult {
    private List<ShopType> data;

    public List<ShopType> getData() {
        return data;
    }

    public void setData(List<ShopType> data) {
        this.data = data;
    }
}

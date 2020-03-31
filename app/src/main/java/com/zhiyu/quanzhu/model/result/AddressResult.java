package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.AddressData;

public class AddressResult extends BaseResult {
    private AddressData data;

    public AddressData getData() {
        return data;
    }

    public void setData(AddressData data) {
        this.data = data;
    }
}

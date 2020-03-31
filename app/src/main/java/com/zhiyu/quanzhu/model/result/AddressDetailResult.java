package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.AddressDetailData;

public class AddressDetailResult extends BaseResult{
    private AddressDetailData data;

    public AddressDetailData getData() {
        return data;
    }

    public void setData(AddressDetailData data) {
        this.data = data;
    }
}

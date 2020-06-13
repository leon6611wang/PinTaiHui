package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CustomerServiceData;

public class CustomerServiceResult extends BaseResult {
    private CustomerServiceData data;

    public CustomerServiceData getData() {
        return data;
    }

    public void setData(CustomerServiceData data) {
        this.data = data;
    }
}

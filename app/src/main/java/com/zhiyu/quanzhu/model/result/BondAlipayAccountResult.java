package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.BondAlipayAcount;

public class BondAlipayAccountResult extends BaseResult {
    private BondAlipayAcount data;

    public BondAlipayAcount getData() {
        return data;
    }

    public void setData(BondAlipayAcount data) {
        this.data = data;
    }
}

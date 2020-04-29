package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Purse;

public class PurseResult extends BaseResult {
    private Purse data;

    public Purse getData() {
        return data;
    }

    public void setData(Purse data) {
        this.data = data;
    }
}

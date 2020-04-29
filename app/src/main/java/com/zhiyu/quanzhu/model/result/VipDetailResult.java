package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.VipDetail;

public class VipDetailResult extends BaseResult {
    private VipDetail data;

    public VipDetail getData() {
        return data;
    }

    public void setData(VipDetail data) {
        this.data = data;
    }
}

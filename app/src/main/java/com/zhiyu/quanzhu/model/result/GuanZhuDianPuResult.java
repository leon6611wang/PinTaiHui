package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.GuanZhuDianPuData;

public class GuanZhuDianPuResult extends BaseResult {
    private GuanZhuDianPuData data;

    public GuanZhuDianPuData getData() {
        return data;
    }

    public void setData(GuanZhuDianPuData data) {
        this.data = data;
    }
}

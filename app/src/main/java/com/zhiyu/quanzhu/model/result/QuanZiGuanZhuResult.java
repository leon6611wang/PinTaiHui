package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.QuanZiGuanZhuData;

public class QuanZiGuanZhuResult extends BaseResult{
    private QuanZiGuanZhuData data;

    public QuanZiGuanZhuData getData() {
        return data;
    }

    public void setData(QuanZiGuanZhuData data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.QuanZiGuanZhuUserData;

public class QuanZiGuanZhuUserResult extends BaseResult {
    private QuanZiGuanZhuUserData data;

    public QuanZiGuanZhuUserData getData() {
        return data;
    }

    public void setData(QuanZiGuanZhuUserData data) {
        this.data = data;
    }
}

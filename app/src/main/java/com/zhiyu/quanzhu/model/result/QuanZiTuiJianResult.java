package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.QuanZiTuiJianData;

public class QuanZiTuiJianResult extends BaseResult {
    private QuanZiTuiJianData data;

    public QuanZiTuiJianData getData() {
        return data;
    }

    public void setData(QuanZiTuiJianData data) {
        this.data = data;
    }
}

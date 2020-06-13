package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.QuanZiShenHeData;

public class QuanZiShenHeResult extends BaseResult {
    private QuanZiShenHeData data;

    public QuanZiShenHeData getData() {
        return data;
    }

    public void setData(QuanZiShenHeData data) {
        this.data = data;
    }
}

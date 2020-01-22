package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.QuanZiTuiJianQuanZiData;

public class QuanZiTuiJianQuanZiResult extends BaseResult {
    private QuanZiTuiJianQuanZiData data;

    public QuanZiTuiJianQuanZiData getData() {
        return data;
    }

    public void setData(QuanZiTuiJianQuanZiData data) {
        this.data = data;
    }
}

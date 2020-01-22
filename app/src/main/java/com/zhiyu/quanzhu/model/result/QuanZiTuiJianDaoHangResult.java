package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.QuanZiTuiJianDaoHangData;

public class QuanZiTuiJianDaoHangResult extends BaseResult{
    private QuanZiTuiJianDaoHangData data;

    public QuanZiTuiJianDaoHangData getData() {
        return data;
    }

    public void setData(QuanZiTuiJianDaoHangData data) {
        this.data = data;
    }
}

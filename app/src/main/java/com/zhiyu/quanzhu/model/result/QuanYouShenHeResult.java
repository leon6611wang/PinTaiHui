package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.QuanYouShenHeData;

public class QuanYouShenHeResult extends BaseResult {
    private QuanYouShenHeData data;

    public QuanYouShenHeData getData() {
        return data;
    }

    public void setData(QuanYouShenHeData data) {
        this.data = data;
    }
}

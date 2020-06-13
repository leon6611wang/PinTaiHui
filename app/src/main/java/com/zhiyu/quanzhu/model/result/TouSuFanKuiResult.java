package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.TouSuFanKuiData;

public class TouSuFanKuiResult extends BaseResult {
    private TouSuFanKuiData data;

    public TouSuFanKuiData getData() {
        return data;
    }

    public void setData(TouSuFanKuiData data) {
        this.data = data;
    }
}

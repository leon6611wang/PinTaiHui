package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.PointExchangeRecordData;

public class PointExchangeRecordResult extends BaseResult {
    private PointExchangeRecordData data;

    public PointExchangeRecordData getData() {
        return data;
    }

    public void setData(PointExchangeRecordData data) {
        this.data = data;
    }
}

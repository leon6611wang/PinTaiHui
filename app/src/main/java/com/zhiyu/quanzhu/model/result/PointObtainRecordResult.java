package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.PointObtainRecordData;

public class PointObtainRecordResult extends BaseResult {
    private PointObtainRecordData data;

    public PointObtainRecordData getData() {
        return data;
    }

    public void setData(PointObtainRecordData data) {
        this.data = data;
    }
}

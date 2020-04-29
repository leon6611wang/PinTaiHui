package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.PurseRecord;
import com.zhiyu.quanzhu.model.data.PurseRecordData;


public class PurseRecordResult extends BaseResult {
    private PurseRecordData data;

    public PurseRecordData getData() {
        return data;
    }

    public void setData(PurseRecordData data) {
        this.data = data;
    }
}

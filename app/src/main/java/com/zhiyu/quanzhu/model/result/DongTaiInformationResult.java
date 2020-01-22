package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.DongTaiInfoData;

public class DongTaiInformationResult extends BaseResult {
    private DongTaiInfoData data;

    public DongTaiInfoData getData() {
        return data;
    }

    public void setData(DongTaiInfoData data) {
        this.data = data;
    }
}

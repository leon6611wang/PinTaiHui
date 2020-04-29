package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.SystemSettingInfo;

public class SystemSettingInfoResult extends BaseResult{
    private SystemSettingInfo data;

    public SystemSettingInfo getData() {
        return data;
    }

    public void setData(SystemSettingInfo data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FriendSetting;

public class FriendSettingResult extends BaseResult {
    private FriendSetting data;

    public FriendSetting getData() {
        return data;
    }

    public void setData(FriendSetting data) {
        this.data = data;
    }
}

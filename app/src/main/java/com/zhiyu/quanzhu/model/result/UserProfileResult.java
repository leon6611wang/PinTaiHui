package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.UserProfileData;

public class UserProfileResult extends BaseResult {
    private UserProfileData data;

    public UserProfileData getData() {
        return data;
    }

    public void setData(UserProfileData data) {
        this.data = data;
    }
}

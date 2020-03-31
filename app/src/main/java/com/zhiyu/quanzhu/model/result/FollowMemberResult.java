package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FollowMemberData;

public class FollowMemberResult extends BaseResult {
    private FollowMemberData data;

    public FollowMemberData getData() {
        return data;
    }

    public void setData(FollowMemberData data) {
        this.data = data;
    }
}

package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.MemberCenter;

public class MemberCenterResult extends BaseResult {
    private MemberCenter data;

    public MemberCenter getData() {
        return data;
    }

    public void setData(MemberCenter data) {
        this.data = data;
    }
}

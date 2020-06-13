package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.MyCardFriendData;

public class MyCardFriendResult extends BaseResult {
    private MyCardFriendData data;

    public MyCardFriendData getData() {
        return data;
    }

    public void setData(MyCardFriendData data) {
        this.data = data;
    }
}

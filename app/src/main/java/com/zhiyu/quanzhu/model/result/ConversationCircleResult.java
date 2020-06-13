package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.ConversationCircle;

public class ConversationCircleResult extends BaseResult {
    private ConversationCircle data;

    public ConversationCircle getData() {
        return data;
    }

    public void setData(ConversationCircle data) {
        this.data = data;
    }
}

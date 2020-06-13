package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ConversationCircleGoodsData;

public class ConversationCircleGoodsResult extends BaseResult {
    private ConversationCircleGoodsData data;

    public ConversationCircleGoodsData getData() {
        return data;
    }

    public void setData(ConversationCircleGoodsData data) {
        this.data = data;
    }
}

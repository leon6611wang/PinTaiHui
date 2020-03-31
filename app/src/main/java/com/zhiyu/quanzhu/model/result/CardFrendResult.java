package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CardFrendData;

public class CardFrendResult extends BaseResult {
    private CardFrendData data;

    public CardFrendData getData() {
        return data;
    }

    public void setData(CardFrendData data) {
        this.data = data;
    }
}

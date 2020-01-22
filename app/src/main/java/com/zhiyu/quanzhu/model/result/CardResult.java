package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CardData;

public class CardResult extends BaseResult {
    private CardData data;

    public CardData getData() {
        return data;
    }

    public void setData(CardData data) {
        this.data = data;
    }
}

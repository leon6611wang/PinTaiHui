package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.Card;

import java.util.List;

public class CardData {
    private Card detail;
    private List<Card> list;

    public List<Card> getList() {
        return list;
    }

    public void setList(List<Card> list) {
        this.list = list;
    }

    public Card getDetail() {
        return detail;
    }

    public void setDetail(Card detail) {
        this.detail = detail;
    }
}

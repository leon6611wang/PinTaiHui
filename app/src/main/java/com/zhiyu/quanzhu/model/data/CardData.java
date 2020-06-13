package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.Card;

import java.util.List;

public class CardData {
    private Card detail;
    private List<Card> list;
    private List<Card> card_list;

    public List<Card> getCard_list() {
        return card_list;
    }

    public void setCard_list(List<Card> card_list) {
        this.card_list = card_list;
    }

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

package com.zhiyu.quanzhu.model.bean;

public class Message {
    private MessageText txt;
    private MessageImage image;
    private MessageGoods goods;
    private MessageOrder order;

    public MessageText getTxt() {
        return txt;
    }

    public void setTxt(MessageText txt) {
        this.txt = txt;
    }

    public MessageImage getImage() {
        return image;
    }

    public void setImage(MessageImage image) {
        this.image = image;
    }

    public MessageGoods getGoods() {
        return goods;
    }

    public void setGoods(MessageGoods goods) {
        this.goods = goods;
    }

    public MessageOrder getOrder() {
        return order;
    }

    public void setOrder(MessageOrder order) {
        this.order = order;
    }
}

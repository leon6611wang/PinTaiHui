package com.leon.chic.model;

public class ServiceMessageMessage {
    private TxtMessage txt;
    private ImageMessage image;
    private GoodsMessage goods;
    private OrderMessage order;

    public TxtMessage getTxt() {
        return txt;
    }

    public void setTxt(TxtMessage txt) {
        this.txt = txt;
    }

    public ImageMessage getImage() {
        return image;
    }

    public void setImage(ImageMessage image) {
        this.image = image;
    }

    public GoodsMessage getGoods() {
        return goods;
    }

    public void setGoods(GoodsMessage goods) {
        this.goods = goods;
    }

    public OrderMessage getOrder() {
        return order;
    }

    public void setOrder(OrderMessage order) {
        this.order = order;
    }
}

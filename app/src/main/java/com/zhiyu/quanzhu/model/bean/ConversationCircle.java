package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class ConversationCircle {
    private String notice;
    private List<String> imgs;
    private List<ConversationCircleShop> shops;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<ConversationCircleShop> getShops() {
        return shops;
    }

    public void setShops(List<ConversationCircleShop> shops) {
        this.shops = shops;
    }
}

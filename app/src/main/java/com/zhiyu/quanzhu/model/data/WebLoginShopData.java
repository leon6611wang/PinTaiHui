package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.WebLoginShop;

import java.util.ArrayList;
import java.util.List;

public class WebLoginShopData {
    private List<WebLoginShop> kefu;
    private List<WebLoginShop> own;
    private List<WebLoginShop> list;

    public List<WebLoginShop> getList() {
        if (null == list) {
            list = new ArrayList<>();
        }
        if (null != own && own.size() > 0) {
            for (WebLoginShop shop : own) {
                shop.setType(1);
            }
            list.addAll(own);
        }
        if (null != kefu && kefu.size() > 0) {
            for (WebLoginShop shop : kefu) {
                shop.setType(2);
            }
            list.addAll(kefu);
        }
        return list;
    }

    public List<WebLoginShop> getKefu() {
        return kefu;
    }

    public void setKefu(List<WebLoginShop> kefu) {
        this.kefu = kefu;
    }

    public List<WebLoginShop> getOwn() {
        return own;
    }

    public void setOwn(List<WebLoginShop> own) {
        this.own = own;
    }
}

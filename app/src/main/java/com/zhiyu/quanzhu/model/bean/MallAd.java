package com.zhiyu.quanzhu.model.bean;

/**
 * 商城广告
 */
public class MallAd {
    private int id;
    private String name;
    private String type;
    private MallAdContent content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MallAdContent getContent() {
        return content;
    }

    public void setContent(MallAdContent content) {
        this.content = content;
    }
}

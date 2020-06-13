package com.zhiyu.quanzhu.model.bean;

/**
 * 商城广告图片
 */
public class MallAdImg {
    private String img;
    private int type;
    private String handle_type;
    private String handle_url;

    @Override
    public String toString() {
        return "img: "+img+" , type: "+type+" , handle_type: "+handle_type+" , handle_url: "+handle_url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHandle_type() {
        return handle_type;
    }

    public void setHandle_type(String handle_type) {
        this.handle_type = handle_type;
    }

    public String getHandle_url() {
        return handle_url;
    }

    public void setHandle_url(String handle_url) {
        this.handle_url = handle_url;
    }
}

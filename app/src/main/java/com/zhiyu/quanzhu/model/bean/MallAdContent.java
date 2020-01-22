package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 商城广告内容
 */
public class MallAdContent {
    private String ad_title;
    private String ad_desc;
    private String ad_tag;
    private int ad_type;
    private List<MallAdImg> ad_imgs;

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public String getAd_desc() {
        return ad_desc;
    }

    public void setAd_desc(String ad_desc) {
        this.ad_desc = ad_desc;
    }

    public String getAd_tag() {
        return ad_tag;
    }

    public void setAd_tag(String ad_tag) {
        this.ad_tag = ad_tag;
    }

    public int getAd_type() {
        return ad_type;
    }

    public void setAd_type(int ad_type) {
        this.ad_type = ad_type;
    }

    public List<MallAdImg> getAd_imgs() {
        return ad_imgs;
    }

    public void setAd_imgs(List<MallAdImg> ad_imgs) {
        this.ad_imgs = ad_imgs;
    }
}

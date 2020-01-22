package com.zhiyu.quanzhu.model.bean;

/**
 * 物流
 */
public class WuLiu {
    private String jiedian;
    private String shijian;

    public WuLiu(String jiedian, String shijian) {
        this.jiedian = jiedian;
        this.shijian = shijian;
    }

    public String getJiedian() {
        return jiedian;
    }

    public void setJiedian(String jiedian) {
        this.jiedian = jiedian;
    }

    public String getShijian() {
        return shijian;
    }

    public void setShijian(String shijian) {
        this.shijian = shijian;
    }
}

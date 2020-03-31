package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 首页数据缓存
 * 圈子，人脉，圈商
 */
@Table(name="home_cache")
public class HomeCache {
    @Column(name="id",isId = true,autoGen = true)
    private int id;
    @Column(name="page_name")
    private String pageName;
    @Column(name="data")
    private String data;
    @Column(name="time")
    private long time;

    public HomeCache(String pageName, String data, long time) {
        this.pageName = pageName;
        this.data = data;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

package com.leon.chic.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "page_data")
public class PageData {
    @Column(name = "id", autoGen = true, isId = true)
    private int id;
    @Column(name = "page_data")
    private String pageData;
    @Column(name = "page_name")
    private String pageName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageData() {
        return pageData;
    }

    public void setPageData(String pageData) {
        this.pageData = pageData;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}

package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class PurseRecordParent {
    private String title;
    private List<PurseRecord> record_list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PurseRecord> getRecord_list() {
        return record_list;
    }

    public void setRecord_list(List<PurseRecord> record_list) {
        this.record_list = record_list;
    }
}

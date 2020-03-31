package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 全局搜索-本地搜索历史
 */
@Table(name = "fullsearch_history")
public class FullSearchHistory {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "history")
    private String history;
    @Column(name = "time")
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

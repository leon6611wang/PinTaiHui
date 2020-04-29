package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.FullSearchArticle;

import java.util.List;

public class FullSearchArticleData {
    private List<FullSearchArticle> acticle_list;
    private List<FullSearchArticle> list;

    public List<FullSearchArticle> getList() {
        return list;
    }

    public void setList(List<FullSearchArticle> list) {
        this.list = list;
    }

    public List<FullSearchArticle> getActicle_list() {
        return acticle_list;
    }

    public void setActicle_list(List<FullSearchArticle> acticle_list) {
        this.acticle_list = acticle_list;
    }
}

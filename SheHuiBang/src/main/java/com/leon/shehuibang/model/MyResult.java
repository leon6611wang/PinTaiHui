package com.leon.shehuibang.model;


import com.leon.shehuibang.base.BaseResult;
import com.leon.shehuibang.model.bean.Comments;
import com.leon.shehuibang.model.bean.CommentsTemplate;

import java.util.List;

public class MyResult extends BaseResult {
    private List<CommentsTemplate> comments_template_list;
    private List<Comments> comments_list;
    private long comments_id;

    public List<Comments> getComments_list() {
        return comments_list;
    }

    public void setComments_list(List<Comments> comments_list) {
        this.comments_list = comments_list;
    }

    public long getComments_id() {
        return comments_id;
    }

    public void setComments_id(long comments_id) {
        this.comments_id = comments_id;
    }

    public List<CommentsTemplate> getComments_template_list() {
        return comments_template_list;
    }

    public void setComments_template_list(List<CommentsTemplate> comments_template_list) {
        this.comments_template_list = comments_template_list;
    }
}

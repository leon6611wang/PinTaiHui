package com.leon.shehuibang.model.bean;

public class CommentsTemplate {
    private long comments_template_id;
    private String template_code;
    private int image_count;

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public long getComments_template_id() {
        return comments_template_id;
    }

    public void setComments_template_id(long comments_template_id) {
        this.comments_template_id = comments_template_id;
    }

    public String getTemplate_code() {
        return template_code;
    }

    public void setTemplate_code(String template_code) {
        this.template_code = template_code;
    }
}

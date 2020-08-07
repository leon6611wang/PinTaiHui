package com.leon.shehuibang.model.bean;

import java.util.List;

public class Comments {
    private long comments_id;
    private long user_id;
    private String txt;
    private List<String> image_list;
    private String template_code;
    private long create_time;

    public String getTemplate_code() {
        return template_code;
    }

    public void setTemplate_code(String template_code) {
        this.template_code = template_code;
    }

    public List<String> getImage_list() {
        return image_list;
    }

    public void setImage_list(List<String> image_list) {
        this.image_list = image_list;
    }

    public long getComments_id() {
        return comments_id;
    }

    public void setComments_id(long comments_id) {
        this.comments_id = comments_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

}

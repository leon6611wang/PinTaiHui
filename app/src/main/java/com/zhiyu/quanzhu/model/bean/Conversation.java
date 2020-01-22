package com.zhiyu.quanzhu.model.bean;

public class Conversation {
    private String user_id;
    private String user_name;
    private String header_pic;

    public Conversation() {
    }

    public Conversation(String user_id, String user_name, String header_pic) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.header_pic = header_pic;
    }

    @Override
    public String toString() {
        return "user_id: "+user_id+" , user_name: "+user_name+" , header_pic: "+header_pic;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHeader_pic() {
        return header_pic;
    }

    public void setHeader_pic(String header_pic) {
        this.header_pic = header_pic;
    }
}

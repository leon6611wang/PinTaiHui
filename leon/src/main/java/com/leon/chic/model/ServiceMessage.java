package com.leon.chic.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "service_message")
public class ServiceMessage {
    @Column(name = "id", autoGen = true, isId = true)
    private int id;
    @Column(name = "msg_type")
    private int msg_type;
    @Column(name = "shop_id")
    private int shop_id;
    @Column(name = "create_time")
    private String create_time;
    @Column(name = "user_avatar")
    private String user_avatar;
    @Column(name = "user_name")
    private String user_name;
    @Column(name = "message_content")
    private String message_content;

    private int silence;

    private ServiceMessageMessage message;

    public int getSilence() {
        return silence;
    }

    public void setSilence(int silence) {
        this.silence = silence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public ServiceMessageMessage getMessage() {
        return message;
    }

    public void setMessage(ServiceMessageMessage message) {
        this.message = message;
    }
}

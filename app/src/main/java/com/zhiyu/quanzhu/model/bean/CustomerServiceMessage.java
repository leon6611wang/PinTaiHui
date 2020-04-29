package com.zhiyu.quanzhu.model.bean;

public class CustomerServiceMessage {
    public static final int TYPE_TEXT = 1, TYPE_IMAGE = 2, TYPE_ORDER = 3, TYPE_GOODS = 4;
    public static final int LEFT=0,RIGHT=1;
    private int id;
    private int message_type;//客服消息类型:文字，图片，商品，订单
    private String message_content;
    private int owner;
    private String time;
    private String userAvatar;
    private String userName;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}

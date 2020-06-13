package com.zhiyu.quanzhu.model.bean;

public class CustomerServiceMessage {
    public static final int TYPE_TEXT = 1, TYPE_IMAGE = 2, TYPE_ORDER = 3, TYPE_GOODS = 4;
    public static final int LEFT = 0, RIGHT = 1;
    private int id;
    private int msg_type;//客服消息类型:文字，图片，商品，订单
    private Message message;
    private int owner;
    private String create_time;
    private String user_avatar;
    private String user_name;
    private int user_id;
    private int shop_id;
    private int from;
    private int uid;
    private int service_id;
    private int unread;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public static int getTypeText() {
        return TYPE_TEXT;
    }


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }


    public int getOwner() {
        if (from == 2) {
            this.owner = 1;
        } else {
            this.owner = 0;
        }
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}

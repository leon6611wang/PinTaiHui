package com.leon.chic.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name="im_user")
public class IMUser {
    @Column(name="user_id",isId = true,autoGen = true)
    private int user_id;
    @Column(name ="id",isId = false)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name ="avatar")
    private String avatar;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

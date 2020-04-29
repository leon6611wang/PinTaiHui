package com.zhiyu.quanzhu.model.bean;

/**
 * 会员中心
 */
public class MemberCenter {
    private String grow_up;
    private String grow_level;
    private int credits;
    private String username;
    private String avatar;

    public String getGrow_up() {
        return grow_up;
    }

    public void setGrow_up(String grow_up) {
        this.grow_up = grow_up;
    }

    public String getGrow_level() {
        return grow_level;
    }

    public void setGrow_level(String grow_level) {
        this.grow_level = grow_level;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
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

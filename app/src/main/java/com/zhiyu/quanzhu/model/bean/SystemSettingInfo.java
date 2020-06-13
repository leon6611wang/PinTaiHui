package com.zhiyu.quanzhu.model.bean;

public class SystemSettingInfo {
    private int silencestatus;
    private int messagestatus;
    private int friendstatus;
    private int contactstatus;
    private String silence_start;
    private String silence_end;
    private String contactstatus_desc;

    public String getContactstatus_desc() {
        switch (this.contactstatus) {
            case 0:
                this.contactstatus_desc = "不开启";
                break;
            case 1:
                this.contactstatus_desc = "公开（任何人可见）";
                break;
            case 2:
                this.contactstatus_desc = "仅圈友可见";
                break;
            case 3:
                this.contactstatus_desc = "仅实名认证用户可见";
                break;
            case 4:
                this.contactstatus_desc = "保密（任何人不可见）";
                break;
        }
        return contactstatus_desc;
    }

    public int getSilencestatus() {
        return silencestatus;
    }

    public void setSilencestatus(int silencestatus) {
        this.silencestatus = silencestatus;
    }

    public int getMessagestatus() {
        return messagestatus;
    }

    public void setMessagestatus(int messagestatus) {
        this.messagestatus = messagestatus;
    }

    public int getFriendstatus() {
        return friendstatus;
    }

    public void setFriendstatus(int friendstatus) {
        this.friendstatus = friendstatus;
    }

    public int getContactstatus() {
        return contactstatus;
    }

    public void setContactstatus(int contactstatus) {
        this.contactstatus = contactstatus;
    }

    public String getSilence_start() {
        return silence_start;
    }

    public void setSilence_start(String silence_start) {
        this.silence_start = silence_start;
    }

    public String getSilence_end() {
        return silence_end;
    }

    public void setSilence_end(String silence_end) {
        this.silence_end = silence_end;
    }
}

package com.zhiyu.quanzhu.model.bean;

/**
 * 消息-系统消息
 */
public class XiTongXiaoXi {
    private int icon;
    private String name;
    private String msg;
    private int msgCount;
    private String time;
    private boolean isGuanFang;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isGuanFang() {
        return isGuanFang;
    }

    public void setGuanFang(boolean guanFang) {
        isGuanFang = guanFang;
    }
}

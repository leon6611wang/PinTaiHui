package com.zhiyu.quanzhu.model.bean;

import java.io.Serializable;

public class MyConversation implements Serializable{
    private String userId;
    private String userName;
    private String headerPic;
    private String messageContent;
    private boolean isRead;
    private long messageTime;
    private boolean isTop;
    private int unreadCount;
    private String type;//group,private
    private int lastMsgId;
    private boolean needRequestUserProfile=true;

    public int getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(int lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    public boolean isNeedRequestUserProfile() {
        return needRequestUserProfile;
    }

    public void setNeedRequestUserProfile(boolean needRequestUserProfile) {
        this.needRequestUserProfile = needRequestUserProfile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeaderPic() {
        return headerPic;
    }

    public void setHeaderPic(String headerPic) {
        this.headerPic = headerPic;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}

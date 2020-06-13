package com.zhiyu.quanzhu.model.bean;

public class JPushRecieverMessage {
    /**
     * 1.客服聊天消息，2.名片更改消息，3.系统消息
     */
    private int message_type;
    private String message_content;

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

}

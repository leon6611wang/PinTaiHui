package com.leon.chic.model;

/**
 * 极光推送消息本地化实体类
 */
public class JPushMessage {
    private int message_type;
    private String message_content;
    private long message_time;

    @Override
    public String toString() {
        return "message_type: "+message_type+" , message_content: "+message_content+" , message_time: "+message_time;
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

    public long getMessage_time() {
        return message_time;
    }

    public void setMessage_time(long message_time) {
        this.message_time = message_time;
    }
}

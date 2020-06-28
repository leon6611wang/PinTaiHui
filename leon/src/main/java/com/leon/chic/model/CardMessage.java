package com.leon.chic.model;

public class CardMessage {
    private int message_type;
    private String message_content;
    private String card;
    private JPushCard jPushCard;
    private long message_time;

    public JPushCard getjPushCard() {
        return jPushCard;
    }

    public void setjPushCard(JPushCard jPushCard) {
        this.jPushCard = jPushCard;
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

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public long getMessage_time() {
        return message_time;
    }

    public void setMessage_time(long message_time) {
        this.message_time = message_time;
    }
}

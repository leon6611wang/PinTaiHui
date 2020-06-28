package com.leon.chic.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 系统消息
 */
@Table(name = "system_message")
public class SystemMessage {
    @Column(name="id",isId = true,autoGen = true)
    private int id;
    @Column(name="message_id")
    private int message_id;
    @Column(name = "message_type")
    private int message_type;
    @Column(name = "message_content")
    private String message_content;
    @Column(name = "message_time")
    private long message_time;
    @Column(name = "read")
    private int read;

    private int unReadCount;

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
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

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}

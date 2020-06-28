package com.leon.chic.listener;

public interface SystemMessageListener {
    void onSystemMessage(int message_type,String message_content, String time, int count);
}

package com.leon.chic.listener;

public interface NotificationActionListener {
    void onNotificationAction(int total_type,int system_type,int id,String avatar,String name,String content,String time);
}

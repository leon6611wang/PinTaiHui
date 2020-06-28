package com.leon.chic.listener;

public interface SystemMessageVariableListener {
    void onSystemMessageVariable(int system_message_type,int id,String img_url,String title,String content,String time);
}

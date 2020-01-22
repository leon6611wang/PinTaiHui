package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 私聊设置
 */
public class ConversationPrivateSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private String targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_private_setting);
        targetId = getIntent().getStringExtra("targetId");
        System.out.println("targetId: " + targetId);
        initViews();
        initConversationData();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("聊天信息");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
        }
    }

    private void initConversationData() {
        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                final int value = conversationNotificationStatus.getValue();
                String title;
                final Conversation.ConversationNotificationStatus conversationNotificationStatus1;
                if(value==1){
                    conversationNotificationStatus1=conversationNotificationStatus.setValue(0);
                    title="免打扰";
                }else{
                    conversationNotificationStatus1=conversationNotificationStatus.setValue(1);
                    title="取消免打扰";
                }

                System.out.println(title);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void removeConversation(){
        RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                System.out.println("删除成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }
}
